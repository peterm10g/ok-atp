package com.lsh.atp.core.task.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;


@Document(indexName = "atp", type = "inventoryRecord")
public class InventoryRecord implements Serializable{

    @Id
    private String id;

    private Long skuId;

    private Integer supplyId;

    private String dc;

    private String zoneCode;

    private Long changeQuantity;

    private Long currentQuantity;

    private Long updatedAt;

    private String sequence_id;

    private String recordType;

    private String updatedFormat;

    @Transient
    private Boolean isRecord;   //是否需要记录

    public InventoryRecord() {
    }

    public InventoryRecord(String id, Long skuId, Integer supplyId, String dc, String zoneCode, Long changeQuantity, Long currentQuantity, Long updatedAt, String sequence_id, String recordType,boolean isRecord) {
        this.id = id;
        this.skuId = skuId;
        this.supplyId = supplyId;
        this.dc = dc;
        this.zoneCode = zoneCode;
        this.changeQuantity = changeQuantity;
        this.currentQuantity = currentQuantity;
        this.updatedAt = updatedAt;
        this.sequence_id = sequence_id;
        this.recordType = recordType;
        this.isRecord = isRecord;
    }

    public InventoryRecord(Long skuId, Integer supplyId, String dc, String zoneCode, Long changeQuantity, Long currentQuantity, Long updatedAt, String sequence_id, String recordType,boolean isRecord) {
        this.skuId = skuId;
        this.supplyId = supplyId;
        this.dc = dc;
        this.zoneCode = zoneCode;
        this.changeQuantity = changeQuantity;
        this.currentQuantity = currentQuantity;
        this.updatedAt = updatedAt;
        this.sequence_id = sequence_id;
        this.recordType = recordType;
        this.isRecord = isRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
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

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public Long getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(Long changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSequence_id() {
        return sequence_id;
    }

    public void setSequence_id(String sequence_id) {
        this.sequence_id = sequence_id;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getUpdatedFormat() {
        return updatedFormat;
    }

    public void setUpdatedFormat(String updatedFormat) {
        this.updatedFormat = updatedFormat;
    }

    public Boolean getRecord() {
        return isRecord;
    }

    public void setRecord(Boolean record) {
        isRecord = record;
    }
}
