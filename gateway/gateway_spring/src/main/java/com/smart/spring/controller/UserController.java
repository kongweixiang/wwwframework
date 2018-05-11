package com.smart.spring.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.gateway.entry.user.User;
import com.smart.gateway.request.UserRequest;
import com.smart.gateway.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    @RequestMapping("/getUser")
    public UserResponse getUser(UserRequest.Params req, HttpServletRequest request) {
        logger.info(gson.toJson(request.getSession()));
        logger.info(gson.toJson(req));
        UserResponse response = new UserResponse();
        User user = new User("123", "sam");
        UserResponse.Params params = new UserResponse.Params();
        params.setUser(user);
        response.setParams(params);
//        throw new RuntimeException();
        return response;
    }
}
