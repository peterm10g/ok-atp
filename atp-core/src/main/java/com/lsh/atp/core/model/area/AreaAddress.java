package com.lsh.atp.core.model.area;

import java.io.Serializable;

/**
 * Project Name: lsh-atp
 * Created by huangdong on 16/7/7.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class AreaAddress implements Serializable {

    private static final long serialVersionUID = 5866408514596018944L;
    /**
     * ID
     */
    private Integer id;

    /**
     * 区域ID
     */
    private Integer areaId;

    /**
     * 编码
     */
    private String districtCode;

    /**
     * 创建时间
     */
    private Integer createdAt;

    /**
     * 修改时间
     */
    private Integer updatedAt;

    /**
     * 是否有效：0-无效；1-有效
     */
    private Integer valid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
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
}
