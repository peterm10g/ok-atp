package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.ItemDetail;
import com.lsh.atp.api.model.baseVo.ZoneTransform;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created by jingyuan on 16/10/18.
 * 查询库存(分DC和出货规则)
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class QueryItemRuleResponse extends BaseResponse implements ZoneTransform{

    @JsonProperty("zone_code")
    private String zoneCode;

    private String channel;

    @JsonProperty("type")
    private Integer type;

    private List<ItemDetail> items;

    public List<ItemDetail> getItems() {
        return items;
    }

    public void setItems(List<ItemDetail> items) {
        this.items = items;
    }

    @Override
    public String getZoneCode() {
        return zoneCode;
    }

    @Override
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }
}
