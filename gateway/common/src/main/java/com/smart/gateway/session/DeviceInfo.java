package com.smart.gateway.session;

public class DeviceInfo {
    private DeviceOs deviceOs;
    private String osVersion;
    private String deviceId;
    private String deviceModel;
    private String deviceResolution;
    private String apkChannel;
    private String deviceToken;
    private String vid;

    public DeviceInfo() {
    }

    public DeviceOs getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(DeviceOs deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceResolution() {
        return deviceResolution;
    }

    public void setDeviceResolution(String deviceResolution) {
        this.deviceResolution = deviceResolution;
    }

    public String getApkChannel() {
        return apkChannel;
    }

    public void setApkChannel(String apkChannel) {
        this.apkChannel = apkChannel;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
