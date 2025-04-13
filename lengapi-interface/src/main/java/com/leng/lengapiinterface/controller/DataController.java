package com.leng.lengapiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.leng.lengapiclientsdk.model.params.NameParams;
import com.leng.lengapiclientsdk.model.response.NameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.leng.lengapiinterface.utils.RequestUtils.get;

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
        log.info("返回响应结果: {}", nameResponse);
        return nameResponse;
    }

    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return get("https://api.vvhan.com/api/love");
    }

    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
    }

}
