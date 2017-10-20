package com.lsh.atp.api.model.inventory;

import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.ZoneTransform;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangqiang on 16/7/6.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class QueryInventory extends BaseResponse implements ZoneTransform {

    @JsonProperty("hold_id")
    private String holdId;

    @JsonProperty("hold_status")
    private Integer holdStatus;


    private String channel;

    @JsonProperty("zone_code")
    private String zoneCode;

    private List<Item> items;

    public QueryInventory() {

    }

    public QueryInventory(Integer status, String meg, Date dataKey, String holdId, Integer holdStatus,String zoneCode, String channel, List<Item> items) {
        super(status, meg, dataKey);
        this.zoneCode = zoneCode;
        this.holdId = holdId;
        this.holdStatus = holdStatus;
        this.channel = channel;
        this.items = items;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String getZoneCode() {
        return zoneCode;
    }

    @Override
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(Integer holdStatus) {
        this.holdStatus = holdStatus;
    }
}
