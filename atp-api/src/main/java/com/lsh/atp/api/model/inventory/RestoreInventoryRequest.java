package com.lsh.atp.api.model.inventory;


import com.lsh.atp.api.model.baseVo.ZoneTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by zhangqiang on 16/7/7.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestoreInventoryRequest implements ZoneTransform,Serializable{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 3219690926028598144L;
    @JsonProperty("area_code")
    private String areaCode;


    @JsonProperty("zone_code")
    private String zoneCode;

    @JsonProperty("sale_area_code")
    private String saleAreaCode;

    private String channel;


    @JsonProperty("hold_id")
    private String holdId;

    private String sequence;

    /**
     * add by zhangqiang 2017/04/06
     * 履约状态 false为未履约 true为履约
     */
    private Boolean isFulfill;


    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
