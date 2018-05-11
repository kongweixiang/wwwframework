package com.smart.servlet.handler;

import com.smart.servlet.context.SmartContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Handlerfinder {

    static Lock lock = new ReentrantLock();

    public static HandlerFactory handlerFactory;

    public static HandlerFactory findHandlerFactory() {
        if (handlerFactory == null) {
            lock.lock();
            if (handlerFactory == null) {
                handlerFactory = new HandlerFactoryImpl();
            }
            lock.unlock();
        }
        return handlerFactory;
    }

}

