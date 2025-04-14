package com.leng.lengapiinterface.utils;

import cn.hutool.http.HttpRequest;
import com.leng.lengapiclientsdk.exception.ApiException;
import com.leng.lengapiclientsdk.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具类
 */
@Slf4j
public class RequestUtils {

    /**
     * 生成url
     *
     * @param baseUrl 基本url
     * @param params  params
     * @return {@link String}
     * @throws ApiException api异常
     */
    public static <T> String buildUrl(String baseUrl, T params) throws ApiException {
        StringBuilder url = new StringBuilder(baseUrl);
        Field[] fields = params.getClass().getDeclaredFields();
        boolean isFirstParam = true;
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            // 跳过serialVersionUID属性
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            try {
                Object value = field.get(params);
                if (value != null) {
                    if (isFirstParam) {
                        url.append("?").append(name).append("=").append(value);
                        isFirstParam = false;
                    } else {
                        url.append("&").append(name).append("=").append(value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new ApiException(ErrorCode.OPERATION_ERROR, "构建url异常");
            }
        }
        return url.toString();
    }


    /**
     * get请求
     *
     * @param baseUrl 基本url
     * @param params  params
     * @return {@link String}
     * @throws ApiException api异常
     */
    public static <T> String get(String baseUrl, T params) throws ApiException {
        return get(buildUrl(baseUrl, params));
    }

    /**
     * get请求
     *
     * @param url url
     * @return {@link String}
     */
    public static String get(String url) {
        String body = HttpRequest.get(url).execute().body();
        log.info("【interface】：请求地址：{}，响应数据：{}", url, body);
        return body;
    }

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return 验证结果
     */
    public static Map<String, Object> validatePhone(String phone) {
        Map<String, Object> result = new HashMap<>();
        if (phone == null || phone.isEmpty()) {
            result.put("success", false);
            result.put("message", "手机号不能为空");
            return result;
        }
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            result.put("success", false);
            result.put("message", "手机号格式错误");
            return result;
        }
        result.put("success", true);
        return result;
    }

    /**
     * 拼接手机号到URL
     *
     * @param baseUrl 基本url
     * @param phone   手机号
     * @return 拼接后的URL
     */
    public static String buildPhoneUrl(String baseUrl, String phone) {
        return baseUrl + phone;
    }

}
