package com.smart.servlet.handler.user;

import com.smart.gateway.request.BaseRequest;
import com.smart.gateway.response.BaseResponse;
import com.smart.servlet.handler.SmartHandler;
import com.smart.servlet.phase.SmartPhase;

public class UserInfoHandler extends SmartHandler {
	@Override
	protected SmartPhase[] getPhases() {
		return new SmartPhase[0];
	}

	@Override
	protected Class<? extends BaseRequest> getRequestClass() {
		return null;
	}

	@Override
	protected Class<? extends BaseResponse> getResponseClass() {
		return null;
	}
}
