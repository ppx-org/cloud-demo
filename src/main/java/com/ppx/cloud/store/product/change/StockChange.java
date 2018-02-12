package com.ppx.cloud.store.product.change;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.store.common.dictionary.Dict;

public class StockChange {
	
	@Id
	private Integer changeId;
	
	private Integer skuId;
	
	private Integer changeNum;
	
	private Integer changeType;
	
	private String changeComment;
	
	private Date created;
	
	private Integer creator;

	public Integer getChangeId() {
		
		System.out.println("xxxxxxgetChangeId:" + changeId);
		return changeId;
	}

	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		
		this.changeNum = changeNum;
	}

	public Integer getChangeType() {
		return changeType;
	}
	
	public String getChangeTypeDesc() {
		return Dict.getChangeTypeDesc(changeType);
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	public String getChangeComment() {
		return changeComment;
	}

	public void setChangeComment(String changeComment) {
		this.changeComment = changeComment;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	

}
	
	
	

