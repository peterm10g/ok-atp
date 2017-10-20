package com.lsh.atp.web.domain;

/**
 * Created by zhangqiang on 17/5/15.
 */
public class InventoryRecodeQuery{

    private Long itemId;

    private String wumartCode;

    private String dc;

    private String queryTime;

    private String recordType;

    public InventoryRecodeQuery() {
    }

    public InventoryRecodeQuery(Long itemId, String wumartCode, String dc, String queryTime, String recordType) {
        this.itemId = itemId;
        this.wumartCode = wumartCode;
        this.dc = dc;
        this.queryTime = queryTime;
        this.recordType = recordType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getWumartCode() {
        return wumartCode;
    }

    public void setWumartCode(String wumartCode) {
        this.wumartCode = wumartCode;
    }
}
