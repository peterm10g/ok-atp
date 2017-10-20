package com.lsh.atp.api.model.baseVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by jingyuan on 16/10/18.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ItemDetail implements Serializable {

    @JsonProperty("item_id")
    private Long itemId;

//    @JsonProperty("total_qty")
//    private Long totalqty;

    @JsonProperty("current_dc")
    private Collection<String> currentDc;

    @JsonProperty("sale_rule")
    private Salerule salerule;

    @JsonProperty("detail_qty")
    private List<ItemDc> itemDcs;

//    @JsonProperty("salerule_list")
//    private List<Salerule> salerules;

//    public ItemDetail(Long itemId, Long totalqty, String currentDc, Salerule salerule, List<ItemDc> itemDcs) {
//        this.itemId = itemId;
//        this.totalqty = totalqty;
//        this.currentDc = currentDc;
//        this.salerule = salerule;
//        this.itemDcs = itemDcs;
//    }
//
//    public ItemDetail(Long itemId, Long totalqty, String currentDc, Salerule salerule, List<ItemDc> itemDcs, List<Salerule> salerules) {
//        this.itemId = itemId;
//        this.totalqty = totalqty;
//        this.currentDc = currentDc;
//        this.salerule = salerule;
//        this.itemDcs = itemDcs;
//        this.salerules = salerules;
//    }


}
