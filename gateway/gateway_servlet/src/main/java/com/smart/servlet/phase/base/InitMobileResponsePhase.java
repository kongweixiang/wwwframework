package com.smart.servlet.phase.base;

import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.exception.BaseException;
import com.smart.gateway.response.BaseResponse;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;

public class InitMobileResponsePhase extends SmartPhase {

	@Override
	public void execute(SmartContext context) throws BaseException {
		Class<? extends BaseResponse> responseClass = (Class<? extends BaseResponse>) context.getAttribute("responseClass");
		try {
			BaseResponse response = responseClass.newInstance();
			Class respParamsClass = responseClass.getMethod("getParams").getReturnType();
			Object respParams = respParamsClass.newInstance();
			response.setParams(respParams);
			context.setAttribute("response", response);
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			throw new BaseException(ReasonCode.get("001", ReasonCode.SystemType.SMART));
		}
	}
}
