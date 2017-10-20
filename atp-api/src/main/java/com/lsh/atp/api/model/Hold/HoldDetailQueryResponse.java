package com.lsh.atp.api.model.Hold;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsh.atp.api.model.baseVo.BaseResponse;
import com.lsh.atp.api.model.baseVo.ItemDc;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangqiang on 16/8/23.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HoldDetailQueryResponse extends BaseResponse {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -1923010309758027064L;
    private String sequence;

    @JsonProperty("hold_status")
    private int holdStatus;

    private int channel;

    private List<ItemDc> items;

    public HoldDetailQueryResponse() {
    }

    public HoldDetailQueryResponse(String sequence, int holdStatus, int channel, List<ItemDc> items) {
        this.sequence = sequence;
        this.holdStatus = holdStatus;
        this.channel = channel;
        this.items = items;
    }

    public HoldDetailQueryResponse(Integer status, String meg, Date dataKey, String sequence, int holdStatus, int channel, List<ItemDc> items) {
        super(status, meg, dataKey);
        this.sequence = sequence;
        this.holdStatus = holdStatus;
        this.channel = channel;
        this.items = items;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(int holdStatus) {
        this.holdStatus = holdStatus;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public List<ItemDc> getItems() {
        return items;
    }

    public void setItems(List<ItemDc> items) {
        this.items = items;
    }
}
