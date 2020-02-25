package com.example.demo.controller;

import com.example.demo.bean.UserInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopController {
    @PostMapping("login")
    public boolean login(@RequestBody UserInfo userInfo) {
        if("lee".equals(userInfo.getUserName())) {
            return true;
        }
        return false;
    }

    @PostMapping("register")
    public boolean register(@RequestBody UserInfo userInfo) {
        if("lee".equals(userInfo.getUserName())) {
            return true;
        }
        return false;
    }
}
