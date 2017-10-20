package com.lsh.atp.api.model.inventory;

import com.lsh.atp.api.model.baseVo.SkuVo;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * lsh-atp
 * Created by peter on 16/9/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventorySyncLshRequest implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1954047515043449883L;

    @NotEmpty
    private String zoneCode;


    private String dcCode;

    @NotEmpty
    private String shipperType;

    /** 平台 **/
    @NotEmpty
    private String system;

    /** 商品 **/
    @Valid
    @Size(min = 1)
    @NotNull
    private List<SkuVo> skuList;

    /**
     * 初始化
     */
    public InventorySyncLshRequest() {
    }

    /**
     * 初始化
     * @param zoneCode zoneCode
     * @param dcCode dcCode
     * @param system system
     * @param skuList skuList
     */
    public InventorySyncLshRequest(String zoneCode, String dcCode, String system, List<SkuVo> skuList) {
        this.zoneCode = zoneCode;
        this.dcCode = dcCode;
        this.system = system;
        this.skuList = skuList;
    }

    public List<SkuVo> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuVo> skuList) {
        this.skuList = skuList;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getDcCode() {
        return dcCode;
    }

    public void setDcCode(String dcCode) {
        this.dcCode = dcCode;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getShipperType() {
        return shipperType;
    }

    public void setShipperType(String shipperType) {
        this.shipperType = shipperType;
    }
}
