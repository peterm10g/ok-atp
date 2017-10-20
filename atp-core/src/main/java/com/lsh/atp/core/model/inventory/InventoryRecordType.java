package com.lsh.atp.core.model.inventory;

/**
 * Created by zhangqiang on 17/5/11.
 */
public enum InventoryRecordType {

    DECREASE("扣减"),

    RESTORE("还原"),

    SYNC("库存同步");

    private String  type;


    InventoryRecordType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
