package com.leng.lengapiclientsdk.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.leng.lengapiclientsdk.client.LengApiClient;
import com.leng.lengapiclientsdk.exception.ApiException;
import com.leng.lengapiclientsdk.exception.ErrorCode;
import com.leng.lengapiclientsdk.exception.ErrorResponse;
import com.leng.lengapiclientsdk.model.request.BaseRequest;
import com.leng.lengapiclientsdk.model.response.ResultResponse;
import com.leng.lengapiclientsdk.utils.SignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础服务类
 */
@Slf4j
@Data
public abstract class BaseService implements ApiService {

    private LengApiClient lengApiClient;

    /**
     * 网关HOST
     */
    private String gatewayHost = "http://localhost:8301";

    private HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * 检查配置
     *
     * @param lengApiClient api客户端
     * @throws ApiException 业务异常
     */
    public void checkConfig(LengApiClient lengApiClient) throws ApiException {
        if (lengApiClient == null && this.getLengApiClient() == null) {
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "请先配置密钥AccessKey/SecretKey");
        }
        if (lengApiClient != null && !StringUtils.isAnyBlank(lengApiClient.getAccessKey(), lengApiClient.getSecretKey())) {
            this.setLengApiClient(lengApiClient);
        }
    }

    /**
     * 执行请求
     *
     * @param request 请求
     * @return {@link HttpResponse}
     * @throws ApiException 业务异常
     */
    private <O, T extends ResultResponse> HttpResponse<String> doRequest(BaseRequest<O, T> request) throws ApiException {
        try {
            return httpClient.send(getHttpRequestByRequestMethod(request), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 通过请求方法获取http响应
     *
     * @param request 要求
     * @return {@link HttpRequest}
     * @throws ApiException 业务异常
     */
    private <O, T extends ResultResponse> HttpRequest getHttpRequestByRequestMethod(BaseRequest<O, T> request) throws ApiException {
        if (ObjectUtils.isEmpty(request)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求参数错误");
        }
        String path = request.getPath().trim();
        String method = request.getMethod().trim().toUpperCase();

        if (ObjectUtils.isEmpty(method)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求方法不存在");
        }
        if (StringUtils.isBlank(path)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求路径不存在");
        }

        String url = splicingGetRequest(request, path);
        log.info("请求方法：{}，请求路径：{}，请求参数：{}", method, path, request.getRequestParams());

        Map<String, String> headers = getHeaders(JSONUtil.toJsonStr(request), lengApiClient);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        // 添加请求头
        headers.forEach(builder::header);

        switch (method) {
            case "GET":
                break;
            case "POST":
                builder.POST(HttpRequest.BodyPublishers.ofString(JSONUtil.toJsonStr(request.getRequestParams())));
                break;
            default:
                throw new ApiException(ErrorCode.OPERATION_ERROR, "不支持该请求方法");
        }

        return builder.build();
    }

    /**
     * 获取响应数据
     *
     * @param request 要求
     * @return {@link T}
     * @throws ApiException 业务异常
     */
    public <O, T extends ResultResponse> T res(BaseRequest<O, T> request) throws ApiException {
        if (lengApiClient == null || StringUtils.isAnyBlank(lengApiClient.getAccessKey(), lengApiClient.getSecretKey())) {
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "请先配置密钥AccessKey/SecretKey");
        }
        T rsp;
        try {
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        HttpResponse<String> httpResponse = doRequest(request);
        String body = httpResponse.body();
        Map<String, Object> data = new HashMap<>();
        if (httpResponse.statusCode() != 200) {
            ErrorResponse errorResponse = JSONUtil.toBean(body, ErrorResponse.class);
            data.put("errorMessage", errorResponse.getMessage());
            data.put("code", errorResponse.getCode());
        } else {
            try {
                // 尝试解析为JSON对象
                data = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                // 解析失败，将body作为普通字符串处理
                data.put("value", body);
            }
        }
        rsp.setData(data);
        return rsp;
    }

    /**
     * 拼接Get请求
     *
     * @param request 要求
     * @param path    路径
     * @return {@link String}
     */
    private <O, T extends ResultResponse> String splicingGetRequest(BaseRequest<O, T> request, String path) {
        // 如果 path 已经是完整 URL（如从数据库读取的 url 字段），直接使用
        if (path.startsWith("http://") || path.startsWith("https://")) {
            // 如果 path 是完整 URL，但不是网关的 URL，重新拼接
            String apiPath;
            if (!path.startsWith(gatewayHost)) {
                apiPath = gatewayHost + path.substring(path.indexOf("/", 8));
            } else {
                apiPath = path;
            }
            
            // 添加查询参数到 URL
            if (!request.getRequestParams().isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(apiPath);
                urlBuilder.append(apiPath.contains("?") ? "&" : "?");
                
                for (Map.Entry<String, Object> entry : request.getRequestParams().entrySet()) {
                    if (entry.getValue() != null) {
                        // 使用 URI 编码处理参数值，避免特殊字符问题
                        String encodedValue = java.net.URLEncoder.encode(String.valueOf(entry.getValue()), 
                                java.nio.charset.StandardCharsets.UTF_8);
                        urlBuilder.append(entry.getKey())
                                .append("=")
                                .append(encodedValue)
                                .append("&");
                    }
                }
                // 删除末尾的 &
                if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
                    urlBuilder.deleteCharAt(urlBuilder.length() - 1);
                }
                
                String finalUrl = urlBuilder.toString();
                log.info("修正后的 GET 请求完整路径：{}", finalUrl);
                return finalUrl;
            }
            
            return apiPath;
        }
        
        // 否则，拼接 gatewayHost 和 path
        StringBuilder urlBuilder = new StringBuilder(gatewayHost);
        if (!gatewayHost.endsWith("/") && !path.startsWith("/")) {
            urlBuilder.append("/");
        } else if (gatewayHost.endsWith("/") && path.startsWith("/")) {
            path = path.substring(1); // 避免双斜杠
        }
        urlBuilder.append(path);

        // 添加查询参数
        if (!request.getRequestParams().isEmpty()) {
            urlBuilder.append(urlBuilder.toString().contains("?") ? "&" : "?");
            
            for (Map.Entry<String, Object> entry : request.getRequestParams().entrySet()) {
                if (entry.getValue() != null) {
                    // 使用 URI 编码处理参数值，避免特殊字符问题
                    String encodedValue = java.net.URLEncoder.encode(String.valueOf(entry.getValue()), 
                            java.nio.charset.StandardCharsets.UTF_8);
                    urlBuilder.append(entry.getKey())
                            .append("=")
                            .append(encodedValue)
                            .append("&");
                }
            }
            // 删除末尾的 &
            if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }
        }
        
        String finalUrl = urlBuilder.toString();
        log.info("修正后的 GET 请求路径：{}", finalUrl);
        return finalUrl;
    }

    /**
     * 获取请求头
     *
     * @param body 请求体
     * @param lengApiClient api客户端
     * @return {@link Map}<{@link String}, {@link String}>
     */
    public Map<String, String> getHeaders(String body, LengApiClient lengApiClient) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json; charset=UTF-8");
        hashMap.put("accessKey", lengApiClient.getAccessKey());
        String encodedBody = SecureUtil.md5(body);
        hashMap.put("body", encodedBody);
        hashMap.put("nonce", String.valueOf(System.currentTimeMillis() % 10000));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.genSign(encodedBody, lengApiClient.getSecretKey()));
        return hashMap;
    }

    @Override
    public <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException {
        try {
            return res(request);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    @Override
    public <O, T extends ResultResponse> T request(LengApiClient lengApiClient, BaseRequest<O, T> request) throws ApiException {
        checkConfig(lengApiClient);
        return request(request);
    }
}