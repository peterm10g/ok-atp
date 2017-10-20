package com.lsh.atp.core.model.hold;

import java.io.Serializable;


public class HoldLog implements Serializable {

	private static final long serialVersionUID = 8218835606580361265L;
	/** 主键 */
    private Long id;
	/** 预占id */
    private Long holdId;
	/** 渠道 */
    private Integer channel;
	/** 操作流水号   如订单id */
    private String sequenceId;
	/** 操作类型 */
    private Integer operationType;
	/** 操作内容 */
    private String content;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;

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

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}
}
