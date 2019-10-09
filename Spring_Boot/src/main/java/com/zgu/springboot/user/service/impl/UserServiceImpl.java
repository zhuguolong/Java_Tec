package com.zgu.springboot.user.service.impl;

import com.zgu.springboot.user.entity.User;
import com.zgu.springboot.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUserInfo(String phone) {
        return new User().setUserName("张三")
                .setGender(1)
                .setAge(23);
    }
}
