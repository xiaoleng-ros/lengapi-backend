package com.leng.lengapigateway;

import com.leng.lengapiclientsdk.utils.SignUtils;
import com.leng.lengapicommon.model.entity.InterfaceInfo;
import com.leng.lengapicommon.model.entity.User;
import com.leng.lengapicommon.model.entity.UserInterfaceInfo;
import com.leng.lengapicommon.service.InnerInterfaceInfoService;
import com.leng.lengapicommon.service.InnerUserInterfaceInfoService;
import com.leng.lengapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList(
            "127.0.0.1",  // 本地测试 IP
            "119.91.248.232",  // 线上环境 IP
            "192.168.0.113"  // 本地wifi IP
    );

    @Value("${api.base-url}")
    private String INTERFACE_HOST;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();

        // 获取客户端的真实 IP
        String sourceAddress = Objects.requireNonNull(request.getRemoteAddress()).getHostString();

        log.info("请求路径：{}，请求方法：{}，来源 IP：{}", path, method, sourceAddress);

        // IP 白名单校验
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(exchange.getResponse());
        }

        // 获取请求头参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = new String(Objects.requireNonNull(headers.getFirst("body"))
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        log.info("请求参数：accessKey={}, nonce={}, timestamp={}, sign={}, body={}",
                accessKey, nonce, timestamp, sign, body);

        // 用户鉴权
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return handleNoAuth(exchange.getResponse());
        }

        if (invokeUser == null) {
            return handleNoAuth(exchange.getResponse());
        }

        // 校验 nonce 和 timestamp
        long nonceValue = 0;
        if (nonce != null) {
            nonceValue = Long.parseLong(nonce);
        }
        long currentTime = System.currentTimeMillis() / 1000;
        if (timestamp != null && (nonceValue > 10000L || (currentTime - Long.parseLong(timestamp)) >= 300)) {
            return handleNoAuth(exchange.getResponse());
        }

        // 校验签名
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign != null && !sign.equals(serverSign)) {
            log.error("签名验证失败：客户端签名={}, 服务端签名={}", sign, serverSign);
            return handleNoAuth(exchange.getResponse());
        }

        // 校验接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("获取接口信息失败", e);
            return handleNoAuth(exchange.getResponse());
        }

        if (interfaceInfo == null) {
            return handleNoAuth(exchange.getResponse());
        }

        // 转发请求并处理响应
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,
                                     Long id, Long userId) {

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            // 修改后的writeWith方法，添加@NonNull注解
            @Override
            @NonNull
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(
                            fluxBody.map(dataBuffer -> {
                                try {
                                    // 更新用户调用剩余次数
                                    innerUserInterfaceInfoService.invokeCount(userId);
                                    // 更新接口总调用次数
                                    innerInterfaceInfoService.addInterfaceTotal(id);
                                } catch (Exception e) {
                                    log.error("更新调用次数失败", e);
                                }
                                // 读取原始数据
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                String data = new String(content, StandardCharsets.UTF_8);
                                log.info("响应结果：{}", data);
                                // 重新包装数据
                                return bufferFactory.wrap(content);
                            })
                    );
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}