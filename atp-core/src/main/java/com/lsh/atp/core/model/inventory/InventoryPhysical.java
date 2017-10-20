package com.lsh.atp.core.model.inventory;

import java.io.Serializable;

/**
 * Created by huangdong on 16/7/7.
 */
public class InventoryPhysical implements Serializable {

    private static final long serialVersionUID = 8808783183072014771L;

    /**
     * ID
     */
    private Long id;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 商品
     */
    private Long skuId;

    /**
     * 库存总数
     */
    private Long skuQuantity;

    /**
     * 创建时间
     */
    private Integer createdAt;

    /**
     * 修改时间
     */
    private Integer updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSkuQuantity() {
        return skuQuantity;
    }

    public void setSkuQuantity(Long skuQuantity) {
        this.skuQuantity = skuQuantity;
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
