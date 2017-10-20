package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.ZoneTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by zhangqiang on 17/7/31.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentDcGetRequest implements ZoneTransform,Serializable{

    @JsonProperty("item_id")
    private Long itemId;

    @JsonProperty("zone_code")
    private String zoneCode;

    @JsonProperty("sale_area_code")
    private String saleAreaCode;

    private String channel;

    @Override
    public String getZoneCode() {
        return this.zoneCode;
    }

    @Override
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }


}
