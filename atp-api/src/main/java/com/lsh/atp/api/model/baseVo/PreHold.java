package com.lsh.atp.api.model.baseVo;

import java.io.Serializable;
import java.util.Map;

/**
 * Project Name: lsh-atp
 * Created by peter on 16/7/6.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class PreHold implements Serializable{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -8338882831901476666L;

    private String holdNo;

    private String holdStatus;

    private String resNo;

    private String zoneCode;

    private Map<Long, String> dcMap;

    public PreHold() {
    }

    public PreHold(String holdNo, String holdStatus) {
        this.holdNo = holdNo;
        this.holdStatus = holdStatus;
    }

    public String getHoldNo() {
        return holdNo;
    }

    public void setHoldNo(String holdNo) {
        this.holdNo = holdNo;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
    }

    public String getResNo() {
        return resNo;
    }

    public void setResNo(String resNo) {
        this.resNo = resNo;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public Map<Long, String> getDcMap() {
        return dcMap;
    }

    public void setDcMap(Map<Long, String> dcMap) {
        this.dcMap = dcMap;
    }
}

