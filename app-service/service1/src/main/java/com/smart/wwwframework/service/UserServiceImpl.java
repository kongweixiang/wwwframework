package com.smart.wwwframework.service;

import com.smart.api.UserService;
import com.smart.dto.User;

public class UserServiceImpl implements UserService {
    @Override
    public User login(String userId) {
        User u = new User();
        u.setId("01");
        u.setName("test");
        return u;
    }
}
