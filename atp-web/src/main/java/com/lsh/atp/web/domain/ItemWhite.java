package com.lsh.atp.web.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zhangqiang on 17/6/14.
 */
public class ItemWhite {

    @JsonProperty("zone_id")
    private String zoneCode;

    @JsonProperty("sku_id")
    private String skuId;

    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("wm_code")
    private String wumartCode;

    @JsonProperty("origin_price")
    private String originPrice;

    @JsonProperty("sale_unit")
    private String saleUnit;

    @JsonProperty("cost_price")
    private String costPrice;

    public ItemWhite() {
    }

    public ItemWhite(String zoneCode, String skuId, String itemId, String wumartCode, String originPrice, String saleUnit, String costPrice) {
        this.zoneCode = zoneCode;
        this.skuId = skuId;
        this.itemId = itemId;
        this.wumartCode = wumartCode;
        this.originPrice = originPrice;
        this.saleUnit = saleUnit;
        this.costPrice = costPrice;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getWumartCode() {
        return wumartCode;
    }

    public void setWumartCode(String wumartCode) {
        this.wumartCode = wumartCode;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }
}
