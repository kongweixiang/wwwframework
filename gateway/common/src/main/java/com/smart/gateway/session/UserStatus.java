package com.smart.gateway.session;

public enum UserStatus {
    EMPTY(""),
    LOGOUT("LOGOUT"),
    LOGIN("LOGIN"),
    TICKOUT("TICKOUT"),
    TIMEOUT("TIMEOUT")
    ;

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
