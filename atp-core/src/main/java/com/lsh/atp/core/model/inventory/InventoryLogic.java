package com.lsh.atp.core.model.inventory;

import java.io.Serializable;
import java.util.Date;

public class InventoryLogic implements Serializable {

	/**  */
    private Long id;
	/** 区域ID */
    private Long areaId;
	/** 商品ID */
    private Long skuId;
	/** 商品批次 */
    private String skuBatch;
	/** 剩余库存数 */
    private Long inventoryQuantity;
	/** 虚拟库存数 */
    private Long virtualNum;
	/** 冻结库存数 */
    private Long freezeNum;
	/** 预占库存数 */
    private Long holdNum;
	/**  */
    private Integer status;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;

    private Integer supplyId;

	private String dc;

	private String zoneCode;

    public InventoryLogic() {
    }

    public InventoryLogic(Long id, Long areaId, Long skuId, String skuBatch, Long inventoryQuantity, Long virtualNum, Long freezeNum, Long holdNum, Integer status, Long createdAt, Long updatedAt, Integer supplyId, String dc, String zoneCode) {
        this.id = id;
        this.areaId = areaId;
        this.skuId = skuId;
        this.skuBatch = skuBatch;
        this.inventoryQuantity = inventoryQuantity;
        this.virtualNum = virtualNum;
        this.freezeNum = freezeNum;
        this.holdNum = holdNum;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.supplyId = supplyId;
        this.dc = dc;
        this.zoneCode = zoneCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuBatch() {
        return skuBatch;
    }

    public void setSkuBatch(String skuBatch) {
        this.skuBatch = skuBatch;
    }

    public Long getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Long inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public Long getVirtualNum() {
        return virtualNum;
    }

    public void setVirtualNum(Long virtualNum) {
        this.virtualNum = virtualNum;
    }

    public Long getFreezeNum() {
        return freezeNum;
    }

    public void setFreezeNum(Long freezeNum) {
        this.freezeNum = freezeNum;
    }

    public Long getHoldNum() {
        return holdNum;
    }

    public void setHoldNum(Long holdNum) {
        this.holdNum = holdNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDC() {
        return dc;
    }

    public void setDC(String DC) {
        this.dc = DC;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
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
}
