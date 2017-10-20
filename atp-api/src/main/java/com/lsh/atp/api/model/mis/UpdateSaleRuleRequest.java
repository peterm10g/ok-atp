package com.lsh.atp.api.model.mis;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/17
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.mis
 * desc:更新出货规则请求model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSaleRuleRequest implements Serializable{

    @JsonProperty("zone_code")
    private String zoneCode;

    @JsonProperty("item_id")
    private Long itemId;

    @JsonProperty("rule_id")
    private Long ruleId;

    public UpdateSaleRuleRequest() {
    }

    public UpdateSaleRuleRequest(String zoneCode, Long itemId, Long ruleId) {
        this.zoneCode = zoneCode;
        this.itemId = itemId;
        this.ruleId = ruleId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
}
