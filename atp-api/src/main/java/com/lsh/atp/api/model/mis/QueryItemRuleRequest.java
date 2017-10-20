package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.ZoneTransform;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jingyuan on 16/10/18.
 * 查询库存(分DC和出货规则)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryItemRuleRequest implements ZoneTransform, Serializable{

    private static final long serialVersionUID = 620204068483333067L;

    @JsonProperty("zone_code")
    private String zoneCode;

    private String channel;

    private List<Long> items;

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String getZoneCode() {
        return zoneCode;
    }

    @Override
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
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
