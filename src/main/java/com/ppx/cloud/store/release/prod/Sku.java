package com.ppx.cloud.store.release.prod;

import java.util.ArrayList;
import java.util.List;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Sku {
	@Id
	private Integer skuId;
	
	private Integer prodId;
	
	private Integer stockNum;
	
	private Float price;
	
	private Integer skuPrio;
	
	private List<SkuImg> skuImgList = new ArrayList<SkuImg>();

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

	public List<SkuImg> getSkuImgList() {
		return skuImgList;
	}

	public void setSkuImgList(List<SkuImg> skuImgList) {
		this.skuImgList = skuImgList;
	}

	
	
	
}
