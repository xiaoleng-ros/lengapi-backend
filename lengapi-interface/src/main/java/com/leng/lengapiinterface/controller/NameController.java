package com.leng.lengapiinterface.controller;


import com.leng.lengapiclientsdk.model.User;
import com.leng.lengapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称 API
 *
 * @author leng
 */

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("leng"));
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping ("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//
//        // todo 实际中要通过 accessKey 查询数据库，判断是否有权限调用该接口
//        if (!accessKey.equals("xiaoleng")) {
//            throw new RuntimeException("无权限");
//        }
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        // todo 时间和当前时间不能超过 5 分钟
//        if (Math.abs(Long.parseLong(timestamp) - System.currentTimeMillis()) > 300000) {
//            throw new RuntimeException("无权限");
//        }
        // todo 实际中通过 header 中的 sign 和数据库中的 secretKey 做比较
//        String severSign = SignUtils.genSign(body, "abcdefg");
//        if (!sign.equals(severSign)) {
//            throw new RuntimeException("无权限");
//        }

        // todo 调用次数 + 1
        String result = "POST 你的名字是" + user.getUsername();
        return result;
    }
}
