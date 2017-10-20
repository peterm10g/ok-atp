package com.lsh.atp.worker.model;

import java.math.BigDecimal;

/**
 * Created by huangdong on 16/7/18.
 */
public class WumartStock {

    private String dc;

    private String wmSkuId;

    private Long lshSkuId;

    private String zdate;

    private String ztime;

    private BigDecimal quality;

    /**
     * zhangqiang add
     *
     * 区域id
     * **/
    private String zoneId;

    public WumartStock() {
    }

    public WumartStock(String dc, String wmSkuId, Long lshSkuId, String zdate, String ztime, BigDecimal quality , String zoneId) {
        this.dc = dc;
        this.wmSkuId = wmSkuId;
        this.lshSkuId = lshSkuId;
        this.zdate = zdate;
        this.ztime = ztime;
        this.quality = quality;
        this.zoneId = zoneId;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getWmSkuId() {
        return wmSkuId;
    }

    public void setWmSkuId(String wmSkuId) {
        this.wmSkuId = wmSkuId;
    }

    public Long getLshSkuId() {
        return lshSkuId;
    }

    public void setLshSkuId(Long lshSkuId) {
        this.lshSkuId = lshSkuId;
    }

    public String getZdate() {
        return zdate;
    }

    public void setZdate(String zdate) {
        this.zdate = zdate;
    }

    public String getZtime() {
        return ztime;
    }

    public void setZtime(String ztime) {
        this.ztime = ztime;
    }

    public BigDecimal getQuality() {
        return quality;
    }

    public void setQuality(BigDecimal quality) {
        this.quality = quality;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
