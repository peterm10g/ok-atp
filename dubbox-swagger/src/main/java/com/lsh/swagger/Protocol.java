package com.lsh.swagger;

/**
 * Created by zhangqiang on 17/5/2.
 */
public enum Protocol {

    REST("rest"),

    RPC("rpc");

    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
