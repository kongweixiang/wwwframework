package com.smart.gateway.session;

public enum Channel {

    H5("H5"),
    NATIVE("NATIVE"),
    WECHAT("WECHAT"),
    OTHER("OTHER");

    private String name;

    Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Channel parse(String name) {
        for (Channel channel : values()) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        return OTHER;
    }
}
