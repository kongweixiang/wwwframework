package com.smart.servlet.phase.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.gateway.util.AppUtil;
import com.smart.gateway.constant.SmartConstant;
import com.smart.gateway.exception.BaseException;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UnDecryptPhase extends SmartPhase {
	Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	@Override
	public void execute(SmartContext context) throws BaseException {
		String jsonReq = null;
		String method = (String) context.getAttribute("method");
		Map<String, String[]> parameterMap = (Map<String, String[]>) context.getAttribute("parameterMap");
		if ("POST".equals(method)) {
			byte[] reqBytes = (byte[]) context.getAttribute("reqBytes");
			try {
				jsonReq = new String(reqBytes, SmartConstant.ENCODING);
				String cmd = (String) context.getAttribute("cmd");
				String sid = (String) context.getAttribute("sid");
				logger.info("Sid:" + sid + ",cmd:" + cmd + ",mobileReq:" + AppUtil.replaceJsonFields(jsonReq, '*'));
			} catch (UnsupportedEncodingException ex) {
				logger.info(ex.getMessage(), ex);
				throw new RuntimeException(ex);
			}
		} else {
			String cmd = (String) context.getAttribute("cmd");
			String sid = (String) context.getAttribute("sid");
			logger.info("Sid:" + sid + ",cmd:" + cmd + ",mobileReq:" + gson.toJson(parameterMap));
		}
		context.setRequest(jsonReq);
	}
}
