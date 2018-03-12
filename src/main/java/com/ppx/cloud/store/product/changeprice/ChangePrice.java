package com.ppx.cloud.store.product.changeprice;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class ChangePrice {
	
	@Id
	private Integer changePriceId;
	
	private Integer skuId;
	
	private Float changePrice;
	
	private String changeComment;
	
	private Date created;
	
	private Integer creator;
	
	

	public Integer getChangePriceId() {
		return changePriceId;
	}

	public void setChangePriceId(Integer changePriceId) {
		this.changePriceId = changePriceId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Float getChangePrice() {
		return changePrice;
	}

	public void setChangePrice(Float changePrice) {
		this.changePrice = changePrice;
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
	
	
	

