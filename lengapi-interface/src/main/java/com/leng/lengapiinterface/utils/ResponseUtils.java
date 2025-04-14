package com.leng.lengapiinterface.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.leng.lengapiclientsdk.exception.ApiException;
import com.leng.lengapiclientsdk.exception.BusinessException;
import com.leng.lengapiclientsdk.exception.ErrorCode;
import com.leng.lengapiclientsdk.model.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应工具类
 */
@Slf4j
public class ResponseUtils {
    public static Map<String, Object> responseToMap(String response) {
        try {
            if (response == null || response.trim().isEmpty()) {
                log.info("收到空响应");
                return new HashMap<>();
            }
            // 检查是否是合法的JSON
            if (response.trim().startsWith("{")) {
                log.info("解析JSON对象响应");
                return new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
                }.getType());
            } else if (response.trim().startsWith("[")) {
                // 处理JSON数组
                log.info("收到JSON数组响应，封装为data字段");
                Map<String, Object> result = new HashMap<>();
                result.put("data", new Gson().fromJson(response, new TypeToken<Object>() {}.getType()));
                return result;
            } else {
                // 不是合法的JSON对象或数组，创建一个包含原始值的Map
                log.info("收到非JSON格式响应，封装为value字段");
                Map<String, Object> result = new HashMap<>();
                result.put("value", response);
                return result;
            }
        } catch (JsonSyntaxException e) {
            // 捕获JSON解析异常
            log.warn("JSON解析失败: {}", e.getMessage());
            Map<String, Object> result = new HashMap<>();
            result.put("value", response);
            return result;
        }
    }

    public static <T> ResultResponse baseResponse(String baseUrl, T params) {
        String response = null;
        try {
            response = RequestUtils.get(baseUrl, params);
            Map<String, Object> fromResponse = responseToMap(response);
            return getResultResponse(fromResponse);
        } catch (ApiException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "构建url异常");
        } catch (Exception e) {
            // 捕获所有其他可能的异常
            log.error("处理响应时发生错误: {}", e.getMessage());
            ResultResponse errorResponse = new ResultResponse();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            errorData.put("originalResponse", response != null ? response : "");
            errorResponse.setData(errorData);
            return errorResponse;
        }
    }

    /**
     * 处理手机号相关的响应
     *
     * @param baseUrl 基本url
     * @param phone   手机号
     * @return {@link ResultResponse}
     */
    public static ResultResponse phoneResponse(String baseUrl, String phone) {
        // 验证手机号
        Map<String, Object> validateResult = RequestUtils.validatePhone(phone);
        if (!(boolean) validateResult.get("success")) {
            log.info("手机号验证失败: {}", validateResult.get("message"));
            ResultResponse response = new ResultResponse();
            response.setData(validateResult);
            return response;
        }
        // 拼接URL
        String url = RequestUtils.buildPhoneUrl(baseUrl, phone);
        log.info("请求手机号信息，URL: {}", url);
        // 调用第三方接口
        String response = RequestUtils.get(url);
        // 处理响应
        Map<String, Object> fromResponse = responseToMap(response);
        return getResultResponse(fromResponse);
    }

    private static ResultResponse getResultResponse(Map<String, Object> fromResponse) {
        ResultResponse baseResponse = new ResultResponse();
        // 直接设置完整的原始响应数据，不做任何处理
        baseResponse.setData(fromResponse);
        return baseResponse;
    }
}
