package com.zgu.springboot.user.service;

import com.zgu.springboot.user.entity.User;

public interface UserService {
    User getUserInfo(String phone);
}
