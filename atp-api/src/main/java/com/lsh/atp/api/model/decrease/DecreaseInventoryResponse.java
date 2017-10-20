package com.lsh.atp.api.model.decrease;

import com.lsh.atp.api.model.baseVo.BaseResponse;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * Project Name: lsh-atp
 * Created by panxudong
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.api.model.Hold.
 * desc:预占接口返回对象
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DecreaseInventoryResponse extends BaseResponse{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -4532746349883939751L;

    @JsonProperty("hold_id")
    private String holdId;

    public DecreaseInventoryResponse() {
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }
}