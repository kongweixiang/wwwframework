/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.gateway.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResponseCode {

	private static Logger logger = Logger.getLogger(ResponseCode.class);
	private static Properties prop = new Properties();
	private static Map<String, String> map = new HashMap<String, String>();
	private String code;
	private ReasonCode reasonCode;

	private ResponseCode() {
	}

	static {
		InputStream in = null;
		try {
			in = ReasonCode.class.getResourceAsStream("/responseCode.properties");
			prop.load(in);
			for (Map.Entry entry : prop.entrySet()) {
				String responseCode = entry.getKey().toString();
				String[] reasonCodes = entry.getValue().toString().split(",");
				for (String reasonCode : reasonCodes) {
					if (map.containsKey(reasonCode)) {
						logger.warn("Some problems with file responseCode.properties");
					}
					map.put(reasonCode, responseCode);
				}
			}
		} catch (IOException ex) {
			logger.info(ex.getMessage(), ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static ResponseCode find(ReasonCode reasonCode) {
		ResponseCode rc = new ResponseCode();
		rc.reasonCode = reasonCode;
		if (map.containsKey(reasonCode.getCode())) {
			rc.code = map.get(reasonCode.getCode());
		} else {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				if (!key.startsWith("regex(")) {
					continue;
				}
				String regex = key.substring(6).trim();
				if (reasonCode.getCode().matches(regex)) {
					rc.code = entry.getValue();
					return rc;
				}
			}
			rc.code = "94";
		}
		return rc;
	}

	/**
	 * @return the reasonCode
	 */
	public ReasonCode getReasonCode() {
		return reasonCode;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
}
