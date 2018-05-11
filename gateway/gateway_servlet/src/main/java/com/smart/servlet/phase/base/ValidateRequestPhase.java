package com.smart.servlet.phase.base;

import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.exception.BaseException;
import com.smart.gateway.request.BaseRequest;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidateRequestPhase extends SmartPhase {

	static ValidatorFactory vf = Validation.buildDefaultValidatorFactory();

	@Override
	public void execute(SmartContext context) throws BaseException {
		BaseRequest request = (BaseRequest) context.getAttribute("request");

		Validator validator = vf.getValidator();
		Set<ConstraintViolation<BaseRequest>> voilationSet = validator.validate(request);
		for (ConstraintViolation<BaseRequest> voilation : voilationSet) {
			logger.info("Filed Error [ " + voilation.getPropertyPath() + " ] [ " + voilation.getInvalidValue() + " ] :" + voilation.getMessage());
			throw new BaseException(ReasonCode.get("020", voilation.getMessage(), ReasonCode.SystemType.SMART, ReasonCode.MessageType.SPECIFIED));
		}
	}
}
