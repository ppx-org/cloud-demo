package com.ppx.cloud.micro.user.order.bean;

public class OverflowSku {
	
	private Integer skuId;
	
	private Integer stockNum;
	
	public OverflowSku(Integer skuId, Integer stockNum) {
		this.skuId = skuId;
		this.stockNum = stockNum;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getStockNum() {
		return stockNum;
	}

	public void setStockNum(Integer stockNum) {
		this.stockNum = stockNum;
	}
	
	
}
