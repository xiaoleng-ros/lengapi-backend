package com.leng.lengapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.leng.lengapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.leng.lengapiclientsdk.utils.SignUtils.genSign;

public class LengapiClient {

    private static final String GATEWAY_HOST = "http://localhost:8301";

    private final String accessKey;

    private final String secretKey;

    public LengapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json; charset=UTF-8");
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);  // 直接将 body 放入,不进行 URL 编码
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }

    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json, "application/json; charset=UTF-8")  // 明确指定请求体的 Content-Type
                .execute();

        System.out.println("HTTP 状态码：" + response.getStatus());
        String result = response.body();
        System.out.println("返回结果内容：" + result);

        String contentType = response.header("Content-Type");
        System.out.println("返回结果格式：Content-Type: " + contentType);

        if (contentType != null && contentType.contains("application/json")) {
            try {
                JSONObject jsonObject = JSONUtil.parseObj(result);
                return jsonObject.toString();
            } catch (Exception e) {
                System.out.println("解析 JSON 失败，返回原始内容");
                return result;
            }
        } else {
            System.out.println("返回内容不是JSON格式，按纯文本返回");
            return result;
        }
    }

}