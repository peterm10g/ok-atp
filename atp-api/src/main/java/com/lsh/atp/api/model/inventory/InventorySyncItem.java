package com.lsh.atp.api.model.inventory;

import java.io.Serializable;

/**
 * Created by huangdong on 16/7/5.
 */
public class InventorySyncItem implements Serializable {

    private static final long serialVersionUID = 8627468570454708544L;

    private Long skuId;

    private String skuBatch;

    private Long skuQuantity;

    public InventorySyncItem() {
    }

    public InventorySyncItem(Long skuId, Long skuQuantity) {
        this.skuId = skuId;
        this.skuQuantity = skuQuantity;
    }

    public InventorySyncItem(Long skuId, String skuBatch, Long skuQuantity) {
        this.skuId = skuId;
        this.skuBatch = skuBatch;
        this.skuQuantity = skuQuantity;
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

    public Long getSkuQuantity() {
        return skuQuantity;
    }

    public void setSkuQuantity(Long skuQuantity) {
        this.skuQuantity = skuQuantity;
    }
}
