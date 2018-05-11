package com.smart.gateway.session;

public enum SessionVersion {

    NEW("NEW"),
    OLD("OLD");

    private String version;

    SessionVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SessionVersion parse(String version) {
        for (SessionVersion sessionVersion : values()) {
            if (sessionVersion.getVersion().equals(version)) {
                return sessionVersion;
            }
        }
        return null;
    }
}
