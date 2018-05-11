package com.smart.gateway.session;

public enum DeviceOs {

    ANDROID("1", "android", "ANDROID"),
    IOS("2", "ios", "IOS"),
    WINDOWSPHONE("3", "WindowsPhone", "WINDOWSPHONE"),
    OTHER("4", "other", "OTHER");

    private String code;
    private String name;
    private String upName;

    private DeviceOs(String code, String name, String upName) {
        this.code = code;
        this.name = name;
        this.upName = upName;
    }

    public DeviceOs parse(String version) {
        for (DeviceOs deviceOs : values()) {
            if (deviceOs.getCode().equals(version)) {
                return deviceOs;
            }
        }
        return OTHER;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpName() {
        return upName;
    }

    public void setUpName(String upName) {
        this.upName = upName;
    }
}
