package com.smart.servlet.phase.base;

import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.exception.BaseException;
import com.smart.gateway.session.Session;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;
import org.apache.commons.lang3.StringUtils;

public class CheckLoginPhase extends SmartPhase {

	@Override
	public void execute(SmartContext context) throws BaseException {
		String std = (String) context.getAttribute("std");
		String vid = (String) context.getAttribute("vid");
		Session session = (Session) context.getAttribute("session");
		if (session.getUserInfo() == null) {
			if (StringUtils.equals("TIMEOUT", session.getUserStatus().getStatus())) {
				throw new BaseException(ReasonCode.get("003", ReasonCode.SystemType.SMART));
			}
			if (StringUtils.equals("KICKOUT", session.getUserStatus().getStatus())) {
				throw new BaseException(ReasonCode.get("004", ReasonCode.SystemType.SMART));
			}
			if (StringUtils.equals("AUTHOUT", session.getUserStatus().getStatus())) {
				throw new BaseException(ReasonCode.get("002", ReasonCode.SystemType.SMART));
			}

			//TODO session登录验证
			throw new BaseException(ReasonCode.get("007", ReasonCode.SystemType.SMART));
		}

	}
}
