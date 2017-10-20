package com.lsh.atp.worker.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangqiang on 16/8/31.
 */
public class KafkaStock {

    private String messageId;
    /** 客户端 */
    private String client;
    /** 物料号，商品唯一编号 */
    private String materialNo;
    /** 客户号，售达方 */
    private String soldToParty;
    /** 工厂 */
    private String plant;
    /** 库存地址 */
    private String storageLocation;
    /** 商品名称（短文本） */
    private String materialDesc;
    /** 商品状态 */
    private String materialState;
    /** 客户的商品编码 */
    private String customerMaterialNo;
    /** 总计已估计库存 */
    private BigDecimal availableNum;
    /** 基本计量单位 */
    private String measuringUnit;
    /** 库存信息最后修改时间 */
    private String lastChangeDatetime;
    /** 最后修改日期 */
    private String lastChangeDate;
    /** 最后修改时间 */
    private String lastChangeTime;

    private String gmtModified;
    /** 扩展字段1 */
    private String temp1;
    /** 扩展字段2 */
    private String temp2;
    /** 扩展字段3 */
    private String temp3;

    private String businessId;

    /**  */
    private Date updateAt;

    public KafkaStock() {
    }

    public KafkaStock(String messageId, String client, String materialNo, String soldToParty, String plant, String storageLocation, String materialDesc, String materialState, String customerMaterialNo, BigDecimal availableNum, String measuringUnit, String lastChangeDatetime, String lastChangeDate, String lastChangeTime, String gmtModified, String temp1, String temp2, String temp3, String businessId, Date updateAt) {
        this.messageId = messageId;
        this.client = client;
        this.materialNo = materialNo;
        this.soldToParty = soldToParty;
        this.plant = plant;
        this.storageLocation = storageLocation;
        this.materialDesc = materialDesc;
        this.materialState = materialState;
        this.customerMaterialNo = customerMaterialNo;
        this.availableNum = availableNum;
        this.measuringUnit = measuringUnit;
        this.lastChangeDatetime = lastChangeDatetime;
        this.lastChangeDate = lastChangeDate;
        this.lastChangeTime = lastChangeTime;
        this.gmtModified = gmtModified;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.businessId = businessId;
        this.updateAt = updateAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getSoldToParty() {
        return soldToParty;
    }

    public void setSoldToParty(String soldToParty) {
        this.soldToParty = soldToParty;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialState() {
        return materialState;
    }

    public void setMaterialState(String materialState) {
        this.materialState = materialState;
    }

    public String getCustomerMaterialNo() {
        return customerMaterialNo;
    }

    public void setCustomerMaterialNo(String customerMaterialNo) {
        this.customerMaterialNo = customerMaterialNo;
    }

    public BigDecimal getAvailableNum() {
        return availableNum;
    }

    public void setAvailableNum(BigDecimal availableNum) {
        this.availableNum = availableNum;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getLastChangeDatetime() {
        return lastChangeDatetime;
    }

    public void setLastChangeDatetime(String lastChangeDatetime) {
        this.lastChangeDatetime = lastChangeDatetime;
    }

    public String getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public String getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(String lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public String getTemp3() {
        return temp3;
    }

    public void setTemp3(String temp3) {
        this.temp3 = temp3;
    }

    public Date getUpdateAt() {
        return (Date) updateAt.clone();
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
