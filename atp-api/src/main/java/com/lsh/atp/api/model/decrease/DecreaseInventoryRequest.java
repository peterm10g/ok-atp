package com.lsh.atp.api.model.decrease;

import com.lsh.atp.api.model.baseVo.Item;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by panxudong
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.model.Hold.
 * desc:预占接口返回对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecreaseInventoryRequest implements Serializable{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -8338882831901476666L;
    /** 区域ID **/

    @JsonProperty("area_code")
    private String areaCode;

    /**
     * 商品区域码  北京 1000,天津 1001
     */
    @JsonProperty("zone_code")
//    @NotBlank
    private String zoneCode;

    /** 渠道 **/
    @NotNull
    private Integer channel;

    /** 渠道流水号 **/
    @NotEmpty
    private String sequence;

    /** 预占ID **/
    @NotEmpty
    @JsonProperty("hold_id")
    private String holdId;

    /** 商品 **/
    @Size(min = 1)
    private List<Item> items;

    public DecreaseInventoryRequest() {
    }

    public DecreaseInventoryRequest(String areaCode, Integer channel, String sequence, String holdId, List<Item> items) {
        this.areaCode = areaCode;
        this.channel = channel;
        this.sequence = sequence;
        this.holdId = holdId;
        this.items = items;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }
}
