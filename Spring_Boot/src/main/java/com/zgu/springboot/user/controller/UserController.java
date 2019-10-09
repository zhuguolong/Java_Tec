package com.zgu.springboot.user.controller;

import com.zgu.springboot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/zgu/v1.0/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getUserInfo")
    public Object getUserInfo(String phone) {
        return userService.getUserInfo(phone);
    }
}
