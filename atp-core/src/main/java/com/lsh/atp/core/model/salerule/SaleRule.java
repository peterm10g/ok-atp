package com.lsh.atp.core.model.salerule;

import java.io.Serializable;

public class SaleRule implements Serializable{
    private Long id;

    private String zoneCode;

    private Long ruleCode;

    private String ruleName;

    private Integer createdAt;

    private Integer updatedAt;

    private Integer valid;

    private Integer type;

    private String supplys;

    public SaleRule() {
    }

    public SaleRule(Long id, String zoneCode, Long ruleCode, String ruleName, Integer createdAt, Integer updatedAt, Integer valid, Integer type, String supplys) {
        this.id = id;
        this.zoneCode = zoneCode;
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.valid = valid;
        this.type = type;
        this.supplys = supplys;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public Long getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(Long ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSupplys() {
        return supplys;
    }

    public void setSupplys(String supplys) {
        this.supplys = supplys;
    }
}