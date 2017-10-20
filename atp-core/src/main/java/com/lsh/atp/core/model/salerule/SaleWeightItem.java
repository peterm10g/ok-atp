package com.lsh.atp.core.model.salerule;

import java.io.Serializable;

/**
 * Created by zhangqiang on 16/12/7.
 */
public class SaleWeightItem implements Serializable{

    private Long id;

    private Long itemId;

    private String zoneCode;

    private String subZoneCode;

    private Integer supplyId;

    private String dc;

    private Integer weight;

    private Integer createdAt;

    private Integer updatedAt;

    public SaleWeightItem(){}

    public SaleWeightItem(Long id, Long itemId, String zoneCode, String subZoneCode, Integer supplyId, String dc, Integer weight, Integer createdAt, Integer updatedAt) {
        this.id = id;
        this.itemId = itemId;
        this.zoneCode = zoneCode;
        this.subZoneCode = subZoneCode;
        this.supplyId = supplyId;
        this.dc = dc;
        this.weight = weight;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode == null ? null : zoneCode.trim();
    }

    public String getSubZoneCode() {
        return subZoneCode;
    }

    public void setSubZoneCode(String subZoneCode) {
        this.subZoneCode = subZoneCode == null ? null : subZoneCode.trim();
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }
}
