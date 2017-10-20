package com.lsh.atp.core.model.supply;

/**
 * 调用商城 获取sku列表 接口返回model
 *
 * Project Name: lsh-atp
 * Created by peter on 16/10/8.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class Item2skuList {

    private Integer ret;

    private SkuContent content;

    private String msg;

    private Integer timestamp;

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public SkuContent getContent() {
        return content;
    }

    public void setContent(SkuContent content) {
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
