package com.smart.spring.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.api.UserService;
import com.smart.gateway.entry.user.User;
import com.smart.gateway.request.UserRequest;
import com.smart.gateway.response.UserResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Resource
    UserService userService;
    @RequestMapping("/getUser")
    public UserResponse getUser(UserRequest.Params req, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        logger.info(gson.toJson(request.getSession()));
        logger.info(gson.toJson(req));
        UserResponse response = new UserResponse();
//        User user = new User("123", "sam");
        com.smart.dto.User user = userService.login("123");
        User user1 = new User();
        PropertyUtils.copyProperties(user1,user);
        UserResponse.Params params = new UserResponse.Params();
        params.setUser(user1);
        response.setParams(params);
//        throw new RuntimeException();
        return response;
    }
}
