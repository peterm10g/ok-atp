package com.lsh.atp.core.model.supply;

import java.util.List;

/**
 * 调用商城 获取sku列表 接口返回model
 *
 * Project Name: lsh-atp
 * Created by peter on 16/10/8.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class SkuContent {

    private Integer total;

    private List<SkuInfo> sku_list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<SkuInfo> getSku_list() {
        return sku_list;
    }

    public void setSku_list(List<SkuInfo> sku_list) {
        this.sku_list = sku_list;
    }
}
