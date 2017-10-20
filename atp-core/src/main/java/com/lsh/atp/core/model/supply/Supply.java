package com.lsh.atp.core.model.supply;

import java.io.Serializable;

/**
 * 商城物美码转链商码返回结果model
 *
 * Project Name: lsh-atp
 * Created by peter on 16/9/29.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class Supply implements Serializable {

    private String market_id;

    private String status;

    private String supplier_id;

    private String code;

    private String item_id;

    private String updated_at;

    public String getMarket_id() {
        return market_id;
    }

    public void setMarket_id(String market_id) {
        this.market_id = market_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
