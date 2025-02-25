package com.leng.lengapiinterface.controller;

import com.leng.lengapiclientsdk.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 名称 API
 *
 * @author leng
 */

@RestController
@RequestMapping("/name")
public class NameController {

    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> getUserNameByPost(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        response.put("username", "POST 你的名字是" + user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
