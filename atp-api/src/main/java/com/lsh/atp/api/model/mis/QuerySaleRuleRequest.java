package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.ZoneTransform;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by jingyuan on 16/10/18.
 * 查询出货规则(根据区域)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuerySaleRuleRequest implements ZoneTransform,Serializable {

    private static final long serialVersionUID = -7636240733753139180L;

    @JsonProperty("zone_code")
    private String zoneCode;

    private String channel;

    /** 类型 1-常温 2-冻品 **/
    private Integer type;

    public QuerySaleRuleRequest() {
    }

    public QuerySaleRuleRequest(String zoneCode, Integer type) {
        this.zoneCode = zoneCode;
        this.type = type;
    }

    public String getZoneCode() {
        return zoneCode;
    }

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
