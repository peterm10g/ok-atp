package com.lsh.atp.core.model.supply;

/**
 *
 * 商城物美码转链商码返回结果model
 *
 * Project Name: lsh-atp
 * Created by peter on 16/9/29.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class WuCode2ItemCode {

    private String ret;

    private Content content;

    private String msg;

    private Integer timestamp;


    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
