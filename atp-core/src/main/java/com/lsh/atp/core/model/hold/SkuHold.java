package com.lsh.atp.core.model.hold;

import java.io.Serializable;
import java.util.List;

public class SkuHold implements Serializable {

	private static final long serialVersionUID = -410789196729403495L;
	/** 预占ID */
    private Long id;
	/** 父预占ID，目前用于秒杀业务处理 */
    private Long pid;
	/** 状态1 正常，2扣减完成 */
    private Integer status;
	/** 预占结束时间 */
    private Long holdEndTime;
	/** 操作流水号，如订单号 */
    private String sequenceId;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	/** 区域ID */
	private Integer areaId;
	/** 渠道 */
	private Integer channel;
	/** 商品区域码 */
	private String zoneCode;
	/** 商品区域码 */
	private String holdNo;

	private List<SkuHoldQty> skuHoldQtyList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<SkuHoldQty> getSkuHoldQtyList() {
		return skuHoldQtyList;
	}

	public void setSkuHoldQtyList(List<SkuHoldQty> skuHoldQtyList) {
		this.skuHoldQtyList = skuHoldQtyList;
	}

	public Long getPid(){
		return this.pid;
	}
	
	public void setPid(Long pid){
		this.pid = pid;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}

	public Long getHoldEndTime() {
		return holdEndTime;
	}

	public void setHoldEndTime(Long holdEndTime) {
		this.holdEndTime = holdEndTime;
	}

	public String getSequenceId(){
		return this.sequenceId;
	}
	
	public void setSequenceId(String sequenceId){
		this.sequenceId = sequenceId;
	}
	
	public Long getCreatedAt(){
		return this.createdAt;
	}
	
	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}
	
	public Long getUpdatedAt(){
		return this.updatedAt;
	}
	
	public void setUpdatedAt(Long updatedAt){
		this.updatedAt = updatedAt;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getHoldNo() {
		return holdNo;
	}

	public void setHoldNo(String holdNo) {
		this.holdNo = holdNo;
	}
}
