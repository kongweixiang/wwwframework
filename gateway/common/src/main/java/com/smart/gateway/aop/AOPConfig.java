package com.smart.gateway.aop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.gateway.interceptor.SmartInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AOPConfig {
	Logger logger = LoggerFactory.getLogger(SmartInterceptor.class);
	Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)" +
			" || @within(org.springframework.stereotype.Controller)")
	private void put(){

	}

//	@Pointcut("execution(* getUser(..))")
//	private void put(){
//
//	}

	@Around("put()")
	public void log(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("进入"+pjp.getTarget().getClass().getSimpleName());
		Object result = pjp.proceed();
		logger.info("return :{}",gson.toJson(result));
	}

	@AfterThrowing(pointcut = "put()",throwing = "e")
	public void logForException(Exception e) throws Throwable {
		logger.info("return Exception:{}",e);
	}
}
