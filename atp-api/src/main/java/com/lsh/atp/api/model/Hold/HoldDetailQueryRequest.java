package com.lsh.atp.api.model.Hold;

import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.api.model.baseVo.ZoneTransform;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 16/8/23.
 */
public class HoldDetailQueryRequest implements ZoneTransform,Serializable{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -4175869214380778747L;

    private String zoneCode;

    private String channel;

    private String sequence;

    private List<ItemDc> items;

    public HoldDetailQueryRequest() {
    }

    public HoldDetailQueryRequest(String zoneCode, String channel, String sequence, List<ItemDc> items) {
        this.zoneCode = zoneCode;
        this.channel = channel;
        this.sequence = sequence;
        this.items = items;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<ItemDc> getItems() {
        return items;
    }

    public void setItems(List<ItemDc> items) {
        this.items = items;
    }
}
