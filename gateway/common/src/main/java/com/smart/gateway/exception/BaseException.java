package com.smart.gateway.exception;

import com.smart.gateway.constant.ReasonCode;
import com.smart.gateway.constant.ResponseCode;

public class BaseException extends RuntimeException {
    private ResponseCode responseCode;

    public BaseException() {
        super();
    }

    public BaseException(ReasonCode reasonCode) {
        super(reasonCode.getMessage());
        this.responseCode = ResponseCode.find(reasonCode);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
