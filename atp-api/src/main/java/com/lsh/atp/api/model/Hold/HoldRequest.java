package com.lsh.atp.api.model.Hold;

import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.ZoneTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.model.Hold.
 * desc:预占接口请求对象
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HoldRequest implements ZoneTransform, Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -8395139533938868928L;
    /**
     * 优供区域码
     */
    @JsonProperty("area_code")
    private String areaCode;

    /**
     * 商品区域码  北京 1000,天津 1001
     */
    @JsonProperty("zone_code")
    private String zoneCode;

    @JsonProperty("sale_area_code")
    private String saleAreaCode;


    /**
     * 预占id,V1.1不再使用
     */
    @JsonProperty("hold_id")
    private String holdId;
    /**
     * 是否扣减
     */
    @JsonProperty("is_decrease")
    private Integer isDecrease;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 操作流水号
     */
    private String sequence;
    /**
     * 预占结束时间
     */
    @JsonProperty("hold_end_time")
    @NotNull
    @Range(min=100,max=2000000000,message="必须传正整数")
    private Long holdEndTime;
    /**
     * 商品列表
     */
    @Size(min = 1,message="item 的个数至少1个")
    @Valid
    @NotNull
    private List<Item> items;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    @Override
    public String toString() {
        return "HoldRequest{" +
                "areaCode='" + areaCode + '\'' +
                ", zoneCode='" + zoneCode + '\'' +
                ", holdId='" + holdId + '\'' +
                ", isDecrease=" + isDecrease +
                ", channel='" + channel + '\'' +
                ", sequence='" + sequence + '\'' +
                ", holdEndTime=" + holdEndTime +
                ", items=" + items +
                '}';
    }
}
