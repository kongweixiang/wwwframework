package com.smart.servlet.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.gateway.request.BaseRequest;
import com.smart.gateway.response.BaseResponse;
import com.smart.servlet.context.SmartContext;
import com.smart.servlet.phase.SmartPhase;

public abstract class SmartHandler {
    private  static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public void handle(SmartContext context) {
        context.setAttribute("requestClass",getRequestClass());
        context.setAttribute("responseClass",getResponseClass());
        SmartPhase[] phases = getPhases();
        for (SmartPhase phase : phases) {
            phase.execute(context);
        }
    }

    public static byte[] renderResponse(SmartContext context) {
        BaseResponse response = (BaseResponse) context.getAttribute("response");
        String resp = gson.toJson(response);
        //TODO 加密等

        return new String(resp).getBytes();
    }

    protected abstract SmartPhase[] getPhases();

    protected abstract Class<? extends BaseRequest> getRequestClass();

    protected abstract Class<? extends BaseResponse> getResponseClass();
}
