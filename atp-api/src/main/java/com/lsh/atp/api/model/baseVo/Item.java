package com.lsh.atp.api.model.baseVo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Project Name: lsh-atp
 * Created by peter on 16/7/6.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Serializable ,Comparable<Item>{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -8338882831901476666L;
    /**
     *  商品id
     */
    @NotNull
    @JsonProperty("item_id")
    private Long itemId;
    /**
     * 商品数量
     */
    private Long qty;


    private String itemType;

    public Item() {
    }

    public Item(Long itemId, Long qty) {
        this.itemId = itemId;
        this.qty = qty;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", qty=" + qty +
                '}';
    }

    public int compare(Item o1, Item o2) {
        return o1.getItemId().compareTo(o2.getItemId());
    }


    public int compareTo(Item o) {
        return this.itemId.compareTo(o.getItemId());
    }
}
