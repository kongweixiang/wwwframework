/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.gateway.constant;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class ReasonCode {

	private static Logger logger = Logger.getLogger(ReasonCode.class);
	private static Properties prop = new Properties();
	private String code;
	private String message;

	static {
		InputStream in = null;
		try {
			in = ReasonCode.class.getResourceAsStream("/reasonCode.properties");
			prop.load(in);
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

	private ReasonCode() {
	}

	// 处理web异常
	public static ReasonCode get(String code, SystemType systemType) {
		String sl = systemType.getPrefix();
		if (StringUtils.equals("4", sl) || StringUtils.equals("a", sl)) {
			ReasonCode rc = new ReasonCode();
			rc.code = systemType.prefix + code;
			String loadMessage = prop.getProperty(rc.code);
			if (StringUtils.isBlank(loadMessage)) {
				loadMessage = "系统错误，请稍后再试";
			}
			rc.setMessage(loadMessage);
			return rc;
		} else {
			return get(code, "系统错误，请稍后再试", systemType, MessageType.DEFAULT, "");
		}
	}

	// 处理srv异常
	public static ReasonCode get(String code, String message) {
		ReasonCode rc = new ReasonCode();
		rc.code = code;
		String loadMessage = prop.getProperty(rc.code, message);
		rc.message = loadMessage;
		return rc;
	}

	/*
	 * 根据后面系统返回的应答码，到本地配置文件中取错误消息
	 */
	public static ReasonCode get(String code, SystemType systemType, String[] params) {
		return get(code, "系统错误，请稍后再试", systemType, MessageType.DEFAULT, "", params);
	}

	public static ReasonCode get(String code, SystemType systemType, String suffix) {
		return get(code, "系统错误，请稍后再试", systemType, MessageType.DEFAULT, suffix);
	}

	/*
	 * 如果后面系统既返回应答码，又返回错误消息，那么调用该方法 1、指定消息 2、配置消息 3、默认消息
	 */
	public static ReasonCode get(String code, String message, SystemType systemType, MessageType mt) {
		return get(code, message, systemType, mt, "");
	}

	/*
	 * 如果后面系统既返回应答码，又返回错误消息，那么调用该方法 1、指定消息 2、配置消息 3、默认消息
	 */
	public static ReasonCode get(String code, String message, SystemType systemType, MessageType mt, String suffix, String... params) {
		ReasonCode rc = new ReasonCode();
		rc.code = systemType.getPrefix() + String.format("%7s", code).replace(' ', '0');
		if (MessageType.SPECIFIED.equals(mt)) {
			rc.setMessage(message);
		} else {
			message = prop.getProperty(rc.code, message);
			rc.setMessage(message);
		}
		rc.setMessage(MessageFormat.format(rc.getMessage(), params) + (suffix == null ? "" : suffix));
		return rc;
	}

	public static ReasonCode get(String code, String message, MessageType mt) {
		ReasonCode rc = new ReasonCode();
		rc.code = code;
		if (MessageType.SPECIFIED.equals(mt)) {
			rc.setMessage(message);
		} else {
			message = prop.getProperty(rc.code, message);
			rc.setMessage(message);
		}
		return rc;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public static enum MessageType {

		DEFAULT, SPECIFIED
	}

	public static enum SystemType {

		SMART("s");
		private String prefix;

		private SystemType(String prefix) {
			this.prefix = prefix;
		}

		/**
		 * @return the prefix
		 */
		public String getPrefix() {
			return prefix;
		}
	}
}
