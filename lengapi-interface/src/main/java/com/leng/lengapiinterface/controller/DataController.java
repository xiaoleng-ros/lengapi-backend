package com.leng.lengapiinterface.controller;

import com.leng.lengapiclientsdk.model.params.IpInfoParams;
import com.leng.lengapiclientsdk.model.params.NameParams;
import com.leng.lengapiclientsdk.model.params.PhoneParams;
import com.leng.lengapiclientsdk.model.params.WeatherParams;
import com.leng.lengapiclientsdk.model.response.NameResponse;
import com.leng.lengapiclientsdk.model.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leng.lengapiinterface.utils.RequestUtils.get;
import static com.leng.lengapiinterface.utils.ResponseUtils.baseResponse;
import static com.leng.lengapiinterface.utils.ResponseUtils.phoneResponse;

/**
 * 名称 API
 *
 * @author leng
 */

@RestController
@RequestMapping("/")
@Slf4j
public class DataController {

    @GetMapping("/name")
    public NameResponse getName(NameParams nameParams) {
        log.info("接收到请求参数: {}", nameParams);
        NameResponse nameResponse = new NameResponse();
        // 将传入的 name 参数设置到响应对象中
        nameResponse.setName("你的名字是" + nameParams.getName());
        return nameResponse;
    }

    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return get("https://api.vvhan.com/api/text/love");
    }

    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
    }

    @GetMapping("/weather")
    public ResultResponse getWeatherInfo(WeatherParams weatherParams) {
        return baseResponse("https://api.vvhan.com/api/weather", weatherParams);
    }

    @GetMapping("/phone")
    public ResultResponse getPhoneInfo(PhoneParams phoneParams) {
        return phoneResponse("https://api.vvhan.com/api/phone/", phoneParams.getPhone());
    }

    @GetMapping("/ipInfo")
    public ResultResponse getIpInfo(IpInfoParams ipInfoParams) {
        return baseResponse("https://api.vvhan.com/api/ipInfo", ipInfoParams);
    }

}
