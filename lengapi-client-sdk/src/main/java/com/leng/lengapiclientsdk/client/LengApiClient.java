package com.leng.lengapiclientsdk.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * leng API 客户端
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LengApiClient {

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;
}