package com.lsh.atp.core.model.supply;

/**
 * 调用商城 获取sku列表 接口返回model
 *
 * Project Name: lsh-atp
 * Created by peter on 16/10/8.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class SkuInfo {

    private String sku_id;

    private Integer sale_unit;

    private String updated_at;

    private Long item_id;

    private String zone_id;

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public Integer getSale_unit() {
        return sale_unit;
    }

    public void setSale_unit(Integer sale_unit) {
        this.sale_unit = sale_unit;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }
}
