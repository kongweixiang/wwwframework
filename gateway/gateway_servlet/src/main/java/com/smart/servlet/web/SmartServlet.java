package com.smart.servlet.web;

import com.smart.gateway.util.BytesUtil;
import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.constant.ResponseCode;
import com.smart.gateway.constant.SysConfig;
import com.smart.gateway.exception.BaseException;
import com.smart.gateway.exception.STDTimeOutException;
import com.smart.gateway.exception.SessionTimeOutException;
import com.smart.gateway.response.BaseResponse;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.handler.HandlerFactory;
import com.smart.servlet.handler.Handlerfinder;
import com.smart.servlet.handler.SmartHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartServlet extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(SmartServlet.class);
    private static HandlerFactory handlerFactory;
    private static Pattern pattern = Pattern.compile(SysConfig.get(SysConfig.URL_REGEX));

    @Override
    public void init() throws ServletException {
        super.init();
        handlerFactory = Handlerfinder.findHandlerFactory();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        byte[] reqBytes = BytesUtil.readBytes(request.getInputStream());
        String clientIp = getClientIp(request);
        String sid = request.getHeader("sid");
        String std = request.getHeader("std");

        SmartContext context = new SmartContext();
        context.setAttribute("requestIp", clientIp);
        context.setAttribute("httpServletRequest", request);
        context.setAttribute("httpServletResponse", response);
        context.setAttribute("reqBytes", reqBytes);
        context.setAttribute("sid", sid);
        context.setAttribute("method", request.getMethod());
        context.setAttribute("std", std);

        if ("GET".equals(request.getMethod())) {
            logger.debug("mobileRequest qs:" + request.getQueryString());
            Map<String, String[]> parameterMap = request.getParameterMap();
            context.setAttribute("parameterMap", parameterMap);
        }

        String requestURI = request.getRequestURI();
        Matcher matcher = pattern.matcher(requestURI);
        if (!matcher.find()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.debug("request uri " + requestURI + " is invalid");
            return;
        }
        String cmd = matcher.group(3);
        context.setAttribute("cmd", cmd);

        SmartHandler handler = handlerFactory.findHandler(cmd);
        if (handler == null) {
            logger.warn("request url:{} not find handler by cmd:{}",requestURI,cmd);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            handler.handle(context);
            BaseResponse mobileResp = (BaseResponse) context.getAttribute("mobileResp");
            context.setAttribute("responseCode", mobileResp.getCode());

        } catch (SessionTimeOutException e) {
            response.setStatus(420);
            return;
        } catch (STDTimeOutException e) {
            response.setStatus(421);
            return;
        } catch (BaseException e) {
            BaseResponse resp = (BaseResponse) context.getAttribute("response");
            ReasonCode reasonCode = e.getResponseCode().getReasonCode();
            resp.setCode(e.getResponseCode().getCode());
            resp.setMessage(reasonCode.getMessage() + "[" + reasonCode.getCode() + "]");
        } catch (Throwable th) {
            logger.error(th.getMessage(), th);
            ReasonCode reasonCode = ReasonCode.get("001", ReasonCode.SystemType.SMART);
            ResponseCode responseCode = ResponseCode.find(reasonCode);
            BaseResponse resp = (BaseResponse) context.getAttribute("response");
            resp.setCode(responseCode.getCode());
            resp.setMessage(reasonCode.getMessage() + "[" + reasonCode.getCode() + "]");
        }

        byte[] resp = SmartHandler.renderResponse(context);
        response.getOutputStream().write(resp);

    }


    private static String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(xff) && !"unKnown".equalsIgnoreCase(xff)) {
            int index = xff.indexOf(",");
            if (index != -1) {
                return xff.substring(0, index);
            } else {
                return xff;
            }
        }
        String xri = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(xri) && !"unKnown".equalsIgnoreCase(xri)) {
            return xri;
        }
        return null;
    }
}
