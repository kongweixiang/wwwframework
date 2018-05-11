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

@SuppressWarnings("rawtypes")
public class ResponseType {

	private static Logger logger = Logger.getLogger(ResponseType.class);
	private static Properties prop = new Properties();
	private static Map<String, String> map = new HashMap<String, String>();

	private ResponseType() {

	}

	static {
		InputStream in = null;
		try {
			in = ReasonCode.class.getResourceAsStream("/responseType.properties");
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

	public static String find(String responseCode) {
		String responseType = "";
		if (map.containsKey(responseCode)) {
			responseType = map.get(responseCode);
		} else {
			responseType = "00";
		}
		return responseType;
	}
}
