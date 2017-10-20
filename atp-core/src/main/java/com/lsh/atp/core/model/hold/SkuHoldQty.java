package com.lsh.atp.core.model.hold;

import java.io.Serializable;

public class SkuHoldQty implements Serializable {

	private static final long serialVersionUID = 6141433136715891655L;
	/**  */
    private Long id;
	/** 预占ID */
    private Long holdId;
	/** 商品ID */
    private Long skuId;
	/** 预占数量 */
    private Long holdQty;

	private Integer supplyId;
	/**
	 * 仓库
	 */
	private String dc;

	private Long restoreQty;

	public SkuHoldQty() {
	}

	public SkuHoldQty(Long id, Long holdId, Long skuId, Long holdQty, Integer supplyId, String dc, Long restoreQty) {
		this.id = id;
		this.holdId = holdId;
		this.skuId = skuId;
		this.holdQty = holdQty;
		this.supplyId = supplyId;
		this.dc = dc;
		this.restoreQty = restoreQty;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHoldId() {
		return holdId;
	}

	public void setHoldId(Long holdId) {
		this.holdId = holdId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getHoldQty() {
		return holdQty;
	}

	public void setHoldQty(Long holdQty) {
		this.holdQty = holdQty;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public Long getRestoreQty() {
		return restoreQty;
	}

	public void setRestoreQty(Long restoreQty) {
		this.restoreQty = restoreQty;
	}

	public Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}
}
