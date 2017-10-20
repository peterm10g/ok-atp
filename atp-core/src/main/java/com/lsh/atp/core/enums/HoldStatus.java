package com.lsh.atp.core.enums;

/**
 * Created by zhangqiang on 17/7/21.
 */
public enum HoldStatus {

    PREHOLD(1,"新建"),

    DEDUCTION(2,"扣减"),

    RESTORE(3,"还原"),

    SUB_RESTORE(4,"部分还原");

    private int key;

    private String name;

    HoldStatus(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
