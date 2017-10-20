package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.ZoneTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/19
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.service.mis
 * desc:更新商品售卖规则request
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateItemRuleRequest implements ZoneTransform,Serializable{

    @JsonProperty("zone_code")
    private String zoneCode;

    @JsonProperty("sub_zone_code")
    private String subZoneCode;

    private String channel;

    @JsonProperty("item_id")
    private Long itemId;

//    @JsonProperty("rule_code")
//    private Long ruleCode;

    private List<String> dcs;

    @JsonProperty("is_new")
    private Integer isNew;

    /** 类型 1-常温品 2-冻品 **/
    private Integer type;



    public String getZoneCode() {
        return zoneCode;
    }

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
