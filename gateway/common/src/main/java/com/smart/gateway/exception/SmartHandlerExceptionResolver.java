package com.smart.gateway.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.constant.ResponseCode;
import com.smart.gateway.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SmartHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        /*  使用response返回    */
        response.setStatus(HttpStatus.OK.value()); //设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
        response.setCharacterEncoding("UTF-8"); //避免乱码
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            if (BaseException.class.isAssignableFrom(ex.getClass())) {
                logger.info("BaseException is swap");
                BaseException e = (BaseException) ex;
                BaseResponse baseResponse = new BaseResponse();
                ReasonCode reasonCode = e.getResponseCode().getReasonCode();
                baseResponse.setCode(reasonCode.getCode());
                baseResponse.setMessage(reasonCode.getMessage());
                response.getOutputStream().write(gson.toJson(baseResponse).getBytes());
            }else {
                logger.info("Exception is swap");
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setCode("99");
                baseResponse.setMessage("详情请咨询");
                response.getOutputStream().write(gson.toJson(baseResponse).getBytes());
            }
        } catch (IOException e) {
        }

        return mv;
    }
}
