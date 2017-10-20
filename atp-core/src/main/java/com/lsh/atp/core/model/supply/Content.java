package com.lsh.atp.core.model.supply;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 商城物美码转链商码返回结果model
 *
 * Project Name: lsh-atp
 * Created by peter on 16/9/29.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class Content implements Serializable {

    private List<Supply> sku_list;

    private Integer total;

    public List<Supply> getSku_list() {
        return sku_list;
    }

    public void setSku_list(List<Supply> sku_list) {
        this.sku_list = sku_list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
