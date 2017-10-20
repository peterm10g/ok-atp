package com.lsh.atp.api.model.baseVo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/8/23.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ItemDc extends Item implements Serializable {

    private String dc;

    public ItemDc() {
        super();
    }

    public ItemDc(Long itemId, Long qty, String dc) {
        super(itemId, qty);
        this.dc = dc;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
