package com.ppx.cloud.store.product.release;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Sku {
	@Id
	private Integer skuId;
	
	private Integer prodId;
	
	private Integer stockNum;
	
	private Float price;
	
	private Integer skuPrio;
	
	private String skuImgSrc;

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getStockNum() {
		return stockNum;
	}

	public void setStockNum(Integer stockNum) {
		this.stockNum = stockNum;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getSkuPrio() {
		return skuPrio;
	}

	public void setSkuPrio(Integer skuPrio) {
		this.skuPrio = skuPrio;
	}

	public String getSkuImgSrc() {
		return skuImgSrc;
	}

	public void setSkuImgSrc(String skuImgSrc) {
		this.skuImgSrc = skuImgSrc;
	}

	
	
}
