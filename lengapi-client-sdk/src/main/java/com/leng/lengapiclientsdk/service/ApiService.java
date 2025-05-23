package com.leng.lengapiclientsdk.service;

import com.leng.lengapiclientsdk.client.LengApiClient;
import com.leng.lengapiclientsdk.exception.ApiException;
import com.leng.lengapiclientsdk.model.request.BaseRequest;
import com.leng.lengapiclientsdk.model.request.IpInfoRequest;
import com.leng.lengapiclientsdk.model.request.PhoneRequest;
import com.leng.lengapiclientsdk.model.request.WeatherRequest;
import com.leng.lengapiclientsdk.model.response.LoveResponse;
import com.leng.lengapiclientsdk.model.response.PoisonousChickenSoupResponse;
import com.leng.lengapiclientsdk.model.response.ResultResponse;

import java.net.http.HttpResponse;

/**
 * API 服务接口
 */
public interface ApiService {

    /**
     * 通用请求
     *
     * @param request 要求
     * @return {@link HttpResponse}
     * @throws ApiException 业务异常
     */

    <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException;

    /**
     * 通用请求
     *
     * @param lengApiClient leng api客户端
     * @param request     要求
     * @return {@link T}
     * @throws ApiException 业务异常
     */
    <O, T extends ResultResponse> T request(LengApiClient lengApiClient, BaseRequest<O, T> request) throws ApiException;

    /**
     * 随意情话
     *
     * @return {@link LoveResponse}
     * @throws ApiException 业务异常
     */
    LoveResponse randomLoveTalk() throws ApiException;

    /**
     * 随意情话
     *
     * @param lengApiClient leng api客户端
     * @return {@link LoveResponse}
     * @throws ApiException 业务异常
     */
    LoveResponse randomLoveTalk(LengApiClient lengApiClient) throws ApiException;

    /**
     * 随机毒鸡汤
     *
     * @return {@link PoisonousChickenSoupResponse}
     * @throws ApiException 业务异常
     */
    PoisonousChickenSoupResponse getPoisonousChickenSoup() throws ApiException;

    /**
     * 随机毒鸡汤
     *
     * @param lengApiClient leng api客户端
     * @return {@link PoisonousChickenSoupResponse}
     * @throws ApiException 业务异常
     */
    PoisonousChickenSoupResponse getPoisonousChickenSoup(LengApiClient lengApiClient) throws ApiException;

    /**
     * 获取天气信息
     *
     * @return {@link ResultResponse}
     * @throws ApiException 业务异常
     */
    ResultResponse getWeatherInfo(WeatherRequest request) throws ApiException;

    /**
     * 获取天气信息
     *
     * @param lengApiClient leng api客户端
     * @return {@link ResultResponse}
     * @throws ApiException 业务异常
     */
    ResultResponse getWeatherInfo(LengApiClient lengApiClient, WeatherRequest request) throws ApiException;

    /**
     * 获取手机号归属地
     *
     * @return {@link ResultResponse}
     * @throws ApiException 业务异常
     */
    ResultResponse getPhoneInfo(PhoneRequest request) throws ApiException;

    /**
     * 获取手机号归属地
     *
     * @param lengApiClient leng api客户端
     * @return {@link ResultResponse}
     * @throws ApiException 业务异常
     */
    ResultResponse getPhoneInfo(LengApiClient lengApiClient, PhoneRequest request) throws ApiException;

    /**
     * 获取IP地址信息
     *
     * @return {@link ResultResponse}
     * @throws ApiException 业务异常
     */
    ResultResponse getIpInfo(IpInfoRequest request) throws ApiException;

    /**
     * 获取IP地址信息
     *
     * @param lengApiClient leng api客户端
     * @return {@link ResultResponse}
     * @throws ApiException 业务异常
     */
    ResultResponse getIpInfo(LengApiClient lengApiClient, IpInfoRequest request) throws ApiException;

}