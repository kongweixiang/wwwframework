package com.smart.gateway.response;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
    protected String code = "00";
    protected String message = "success";
    private T params;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
