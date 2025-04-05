package com.leng.lengapiclientsdk.service;

import com.leng.lengapiclientsdk.client.LengApiClient;
import com.leng.lengapiclientsdk.exception.ApiException;
import com.leng.lengapiclientsdk.model.request.BaseRequest;
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
     * @param lengApiClient qi api客户端
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
     * @param lengApiClient qi api客户端
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
     * 喝毒鸡汤
     *
     * @param lengApiClient qi api客户端
     * @return {@link PoisonousChickenSoupResponse}
     * @throws ApiException 业务异常
     */
    PoisonousChickenSoupResponse getPoisonousChickenSoup(LengApiClient lengApiClient) throws ApiException;

}