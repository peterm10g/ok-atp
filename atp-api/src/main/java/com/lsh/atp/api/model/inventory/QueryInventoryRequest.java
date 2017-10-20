package com.lsh.atp.api.model.inventory;


import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.ZoneTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 16/7/4.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryInventoryRequest implements ZoneTransform,Serializable{

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1954047515043444183L;
    /** 区域ID **/
    @JsonProperty("area_code")
    private String areaCode;

    @JsonProperty("zone_code")
    private String zoneCode;

    @JsonProperty("sale_area_code")
    private String saleAreaCode;

    /** 渠道 **/
    private String channel;

    /** 预占ID **/
    //@JsonProperty("hold_id")
    @JsonIgnore
    private String holdID;

    /** 商品 **/
    private List<Item> items;
}
