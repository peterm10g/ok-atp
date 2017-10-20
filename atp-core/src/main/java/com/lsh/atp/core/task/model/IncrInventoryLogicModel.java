package com.lsh.atp.core.task.model;

import com.lsh.atp.core.model.hold.SkuHoldQty;

import java.util.List;

/**
 * Created by zhangqiang on 16/12/14.
 */
public class IncrInventoryLogicModel {

    private String sequence;

    private String zoneCode;

    private List<SkuHoldQty> skuHoldQtyList;

    private Operation operatation;


    public IncrInventoryLogicModel(String sequence, String zoneCode, List<SkuHoldQty> skuHoldQtyList, Operation operatation) {
        this.sequence = sequence;
        this.zoneCode = zoneCode;
        this.skuHoldQtyList = skuHoldQtyList;
        this.operatation = operatation;
    }

    public enum Operation{
        DCREASE,RESTORE
    }



    public Operation getOperatation() {
        return operatation;
    }

    public void setOperatation(Operation operatation) {
        this.operatation = operatation;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public List<SkuHoldQty> getSkuHoldQtyList() {
        return skuHoldQtyList;
    }

    public void setSkuHoldQtyList(List<SkuHoldQty> skuHoldQtyList) {
        this.skuHoldQtyList = skuHoldQtyList;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
