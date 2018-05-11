package com.smart.servlet.phase.base;

import com.smart.gateway.exception.BaseException;
import com.smart.gateway.exception.SessionTimeOutException;
import com.smart.gateway.session.Session;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;
import org.apache.commons.lang3.StringUtils;

public class RecoverSessionPhase extends SmartPhase {

	@Override
	public void execute(SmartContext context) throws BaseException {
		String sid = (String) context.getAttribute("sid");
		if (StringUtils.isBlank(sid)) {
			throw new SessionTimeOutException();
		}

		Session session = new Session();
		session.setSid(sid);

		//todo seesion 的获取，包括一些其他session信息 如DeviceInfo,UserInfo,UserStatus等等系统自定义的信息

		context.setAttribute("session", session);
	}
}
