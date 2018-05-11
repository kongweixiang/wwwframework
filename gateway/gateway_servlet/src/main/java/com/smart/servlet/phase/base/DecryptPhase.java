package com.smart.servlet.phase.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.gateway.util.AppUtil;
import com.smart.gateway.exception.BaseException;
import com.smart.gateway.exception.STDTimeOutException;
import com.smart.gateway.exception.SessionTimeOutException;
import com.smart.gateway.session.Session;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class DecryptPhase extends SmartPhase {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	@Override
	public void execute(SmartContext context) throws BaseException {
		String jsonReq = null;
		String method = (String) context.getAttribute("method");
		Map<String, String[]> parameterMap = (Map<String, String[]>) context.getAttribute("parameterMap");
		Session session = (Session) context.getAttribute("session");
		if (session == null) {
			throw new SessionTimeOutException();
		}
		if (session.getStd() == null) {
			throw new STDTimeOutException();
		}
		String std = session.getStd();
		if ("GET".equals(method)) {
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				String[] values = entry.getValue();
				for (int i = 0; i < values.length; i++) {
					try {
						values[i] = AppUtil.decryptData(values[i], std);
					} catch (UnsupportedEncodingException ex) {
						logger.info(ex.getMessage(), ex);
						throw new RuntimeException(ex);
					}
				}
			}
			String cmd = (String) context.getAttribute("cmd");
			String sid = (String) context.getAttribute("sid");
			logger.info("Sid:" + sid + ",cmd:" + cmd + ",mobileReq:" + gson.toJson(parameterMap));
		} else {
			byte[] reqBytes = (byte[]) context.getAttribute("reqBytes");
			try {
				jsonReq = AppUtil.decryptData(new String(reqBytes), std);
				String cmd = (String) context.getAttribute("cmd");
				String sid = (String) context.getAttribute("sid");
				logger.info("Sid:" + sid + ",cmd:" + cmd + ",mobileReq:" + AppUtil.replaceJsonFields(jsonReq, '*'));
			} catch (UnsupportedEncodingException ex) {
				logger.info(ex.getMessage(), ex);
				throw new RuntimeException(ex);
			}
		}
		context.setRequest(jsonReq);
	}
}
