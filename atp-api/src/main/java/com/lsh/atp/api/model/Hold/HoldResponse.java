package com.lsh.atp.api.model.Hold;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.Item;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.model.Hold.
 * desc:预占接口返回对象
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HoldResponse extends BaseResponse {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1638278451727725951L;
    /**
     * 预占id
     */
    @JsonProperty("hold_id")
    private String holdId;

    private List<String> skuIdList;

    private List<Item> itemList;

    private String holdStatus;

    public HoldResponse() {
    }

    public HoldResponse(Integer status, String meg, Date dataKey, String holdId, List<String> skuidList) {
        super(status, meg, dataKey);
        this.holdId = holdId;
        this.skuIdList = skuidList;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public List<String> getSkuIdList() {
        return skuIdList;
    }

    public void setSkuIdList(List<String> skuIdList) {
        this.skuIdList = skuIdList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
    }

    @Override
    public String toString() {
        return "HoldResponse{" +
                "holdId='" + holdId + '\'' +
                ", skuIdList=" + skuIdList +
                '}';
    }


}
