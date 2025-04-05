package com.leng.lengapiclientsdk.config;

import com.leng.lengapiclientsdk.client.LengApiClient;
import com.leng.lengapiclientsdk.service.ApiService;
import com.leng.lengapiclientsdk.service.impl.ApiServiceImpl;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * leng API 客户端配置
 */
@Data
@Configuration
@ConfigurationProperties("leng.api.client")
@ComponentScan
public class LengApiClientConfig {

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;

    /**
     * 网关
     */
    private String host;

    @Bean
    public LengApiClient lengApiClient() {
        return new LengApiClient(accessKey, secretKey);
    }

    @Bean
    public ApiService apiService() {
        ApiServiceImpl apiService = new ApiServiceImpl();
        apiService.setLengApiClient(new LengApiClient(accessKey, secretKey));
        if (StringUtils.isNotBlank(host)) {
            apiService.setGatewayHost(host);
        }
        return apiService;
    }
}