package com.lsh.atp.api.model.mis;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.Salerule;
import com.lsh.atp.api.model.baseVo.ZoneTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created by jingyuan on 16/10/18.
 * 查询出货规则(根据区域)
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class QuerySaleRuleResponse extends BaseResponse implements ZoneTransform{

    @JsonProperty("zone_code")
    private String zoneCode;

    private String channel;

//    @JsonProperty("sale_rule")
//    private List<Salerule> salerules;

    @JsonProperty("sale_rule")
    private List<WarehouseDto> salerules;

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
