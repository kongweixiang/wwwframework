package com.smart.servlet.handler;

import com.smart.servlet.handler.user.UserInfoHandler;

import java.util.concurrent.ConcurrentHashMap;

public class HandlerFactoryImpl implements HandlerFactory{

    private static ConcurrentHashMap<String,SmartHandler> handlers = new ConcurrentHashMap<>();

    //命令字
    private static final String TEST_CMD = "test";
    private static final String USER_INFO_CMD = "userInfo";

    //命令字和handler
    {
        handlers.put(TEST_CMD,new TestHandler());
        handlers.put(USER_INFO_CMD,new UserInfoHandler());
    }
    public HandlerFactoryImpl(){
        super();
    }

    @Override
    public SmartHandler findHandler(String cmd) {
        return handlers.get(cmd);
    }
}
