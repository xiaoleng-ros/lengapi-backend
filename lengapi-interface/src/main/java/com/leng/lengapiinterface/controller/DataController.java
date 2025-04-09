package com.leng.lengapiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.leng.lengapiclientsdk.model.params.NameParams;
import com.leng.lengapiclientsdk.model.response.NameResponse;
import org.springframework.web.bind.annotation.*;

import static com.leng.lengapiinterface.utils.RequestUtils.get;

/**
 * 名称 API
 *
 * @author leng
 */

@RestController
@RequestMapping("/")
public class DataController {

    @PostMapping("/name")
    public NameResponse getName(@RequestBody NameParams nameParams) {
        NameResponse response = new NameResponse();
        response.setUsername(nameParams.getUsername());
        return response;
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
