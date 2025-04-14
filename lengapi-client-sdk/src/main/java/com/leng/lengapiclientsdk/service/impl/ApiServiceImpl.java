package com.leng.lengapiclientsdk.service.impl;

import com.leng.lengapiclientsdk.client.LengApiClient;
import com.leng.lengapiclientsdk.exception.ApiException;
import com.leng.lengapiclientsdk.model.request.*;
import com.leng.lengapiclientsdk.model.response.LoveResponse;
import com.leng.lengapiclientsdk.model.response.PoisonousChickenSoupResponse;
import com.leng.lengapiclientsdk.model.response.ResultResponse;
import com.leng.lengapiclientsdk.service.ApiService;
import com.leng.lengapiclientsdk.service.BaseService;

/**
 * API 服务实现类
 */
public class ApiServiceImpl extends BaseService implements ApiService {

    @Override
    public LoveResponse randomLoveTalk() throws ApiException {
        LoveRequest request = new LoveRequest();
        return request(request);
    }

    @Override
    public LoveResponse randomLoveTalk(LengApiClient lengApiClient) throws ApiException {
        LoveRequest request = new LoveRequest();
        return request(lengApiClient, request);
    }

    @Override
    public PoisonousChickenSoupResponse getPoisonousChickenSoup() throws ApiException {
        PoisonousChickenSoupRequest request = new PoisonousChickenSoupRequest();
        return request(request);
    }

    @Override
    public PoisonousChickenSoupResponse getPoisonousChickenSoup(LengApiClient lengApiClient) throws ApiException {
        PoisonousChickenSoupRequest request = new PoisonousChickenSoupRequest();
        return request(lengApiClient, request);
    }

    @Override
    public ResultResponse getWeatherInfo(LengApiClient lengApiClient, WeatherRequest request) throws ApiException {
        return request(lengApiClient, request);
    }

    @Override
    public ResultResponse getWeatherInfo(WeatherRequest request) throws ApiException {
        return request(request);
    }

    @Override
    public ResultResponse getPhoneInfo(LengApiClient lengApiClient, PhoneRequest request) throws ApiException {
        return request(lengApiClient, request);
    }

    @Override
    public ResultResponse getPhoneInfo(PhoneRequest request) throws ApiException {
        return request(request);
    }

    @Override
    public ResultResponse getIpInfo(LengApiClient lengApiClient, IpInfoRequest request) throws ApiException {
        return request(lengApiClient, request);
    }

    @Override
    public ResultResponse getIpInfo(IpInfoRequest request) throws ApiException {
        return request(request);
    }
}