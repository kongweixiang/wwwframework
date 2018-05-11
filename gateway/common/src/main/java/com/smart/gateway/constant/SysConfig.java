/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.gateway.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SysConfig {

	private static Logger logger = Logger.getLogger(SysConfig.class);

	public static final String URL_REGEX = "url_regex";
	public static final String KEY_TOP_PK = "key_top_pk";

	private static Properties prop = new Properties();

	static {
		InputStream in = null;
		try {
			in = SysConfig.class.getResourceAsStream("/system.properties");
			prop.load(in);
		} catch (IOException ex) {
			logger.info("Some problems with system.properties");
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

	public static String get(String key) {
		return prop.getProperty(key);
	}
}
