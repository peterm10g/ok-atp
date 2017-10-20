package com.lsh.atp.core.model.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/20
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.model
 * desc:批量同步库存model
 */
public class BulkWumarkStock {

    /** 状态 **/
    private Integer status;

    private List<InventoryLogic> list;

    private String dc;

    public BulkWumarkStock(Integer status, List<InventoryLogic> list, String dc) {
        this.status = status;
        //this.list = list;
        this.dc = dc;
        this.list = new ArrayList<InventoryLogic>(list);    //浅复制
    }

    public BulkWumarkStock() {

    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<InventoryLogic> getList() {
        return list;
    }

    public void setList(List<InventoryLogic> list) {
        this.list = list;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
