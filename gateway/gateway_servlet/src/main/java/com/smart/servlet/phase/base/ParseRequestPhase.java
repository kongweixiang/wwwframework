package com.smart.servlet.phase.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.exception.BaseException;
import com.smart.gateway.request.BaseRequest;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

public class ParseRequestPhase extends SmartPhase {
	Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	@Override
	public void execute(SmartContext context) throws BaseException {
		Class<? extends BaseRequest> requestClass = (Class<? extends BaseRequest>) context.getAttribute("requestClass");
		BaseRequest request = null;
		try {
			request = requestClass.newInstance();
			request.setCmd((String) context.getAttribute("cmd"));
			request.setV((String) context.getAttribute("v"));
			Class paramsClass = requestClass.getMethod("getParams").getReturnType();
			String method = (String) context.getAttribute("method");
			if ("GET".equals(method)) {
				Map<String, String[]> parameterMap = (Map<String, String[]>) context.getAttribute("parameterMap");
				Object params = paramsClass.newInstance();
				request.setParams(params);
				for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
					String key = entry.getKey();
					String[] values = entry.getValue();
					Method wm = findSetMethod(paramsClass, key);
					if (wm != null) {
						wm.invoke(params, values[0]);
					}
				}
			} else {
				String jsonReq = context.getRequest();
				request.setParams(gson.fromJson(jsonReq, paramsClass));
			}
		} catch (JsonSyntaxException ex) {
			logger.info(ex.getMessage(), ex);
			throw new BaseException(ReasonCode.get("006", ReasonCode.SystemType.SMART)); // 报文格式错
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
		context.setAttribute("request", request);
	}

	private Method findSetMethod(Class c, String fieldName) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(c);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			if (fieldName.equals(pd.getName())) {
				return pd.getWriteMethod();
			}
		}
		return null;
	}
}
