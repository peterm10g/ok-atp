package com.lsh.atp.api.model.baseVo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Project Name: lsh-atp
 * Created by peter on 16/7/6.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkuVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -8338882831901986666L;
    /**
     *  商品id(改为物美码)
     */
    @NotNull
    private String itemId;
    /**
     * 商品数量
     */
    @NotNull
    private BigDecimal qty;

    public SkuVo() {
    }

    public SkuVo(String itemId, BigDecimal qty) {
        this.itemId = itemId;
        this.qty = qty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
