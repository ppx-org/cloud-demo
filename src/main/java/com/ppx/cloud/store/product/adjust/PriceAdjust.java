package com.ppx.cloud.store.product.adjust;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.store.common.dictionary.Dict;

public class PriceAdjust {
	/*
	 * ADJUST_ID		int not null auto_increment,
	SKU_ID			int not null,
	ADJUST_RPICE	decimal(7,2) not null,
	ADJUST_COMMENT	varchar(128),
	CREATED         timestamp not null default CURRENT_TIMESTAMP,
	 */
	
	@Id
	private Integer adjustId;
	
	private Integer skuId;
	
	private Integer adjustPrice;
	
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

	public Integer getAdjustPrice() {
		return adjustPrice;
	}

	public void setAdjustPrice(Integer adjustPrice) {
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
	
	
	

