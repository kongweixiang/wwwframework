package com.smart.servlet.handler;

public interface HandlerFactory {

    SmartHandler findHandler(String cmd);
}
