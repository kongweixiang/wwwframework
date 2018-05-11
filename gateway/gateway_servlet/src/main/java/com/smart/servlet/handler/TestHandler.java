package com.smart.servlet.handler;

import com.smart.gateway.request.BaseRequest;
import com.smart.gateway.response.BaseResponse;
import com.smart.servlet.phase.SmartPhase;
import com.smart.servlet.phase.TestPhase;
import com.smart.servlet.phase.base.*;

public class TestHandler extends SmartHandler {
    @Override
    protected SmartPhase[] getPhases() {
        return new SmartPhase[]{
                new InitMobileResponsePhase(),
                new RecoverSessionPhase(),
                new CheckLoginPhase(),
                new DecryptPhase(),
                new ParseRequestPhase(),
                new ValidateRequestPhase(),
                new TestPhase()
        };
    }

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return BaseRequest.class;
    }

    @Override
    protected Class<? extends BaseResponse> getResponseClass() {
        return BaseResponse.class;
    }
}
