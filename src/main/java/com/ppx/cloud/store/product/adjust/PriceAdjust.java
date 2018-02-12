package com.ppx.cloud.store.product.adjust;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class PriceAdjust {
	
	@Id
	private Integer adjustId;
	
	private Integer skuId;
	
	private Float adjustPrice;
	
	private String adjustComment;
	
	private Date created;

	public Integer getAdjustId() {
		return adjustId;
	}

	public void setAdjustId(Integer adjustId) {
		this.adjustId = adjustId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Float getAdjustPrice() {
		return adjustPrice;
	}

	public void setAdjustPrice(Float adjustPrice) {
		this.adjustPrice = adjustPrice;
	}

	public String getAdjustComment() {
		return adjustComment;
	}

	public void setAdjustComment(String adjustComment) {
		this.adjustComment = adjustComment;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
	
	

}
	
	
	

