package com.ppx.cloud.store.release.prod;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class SkuImg {
	@Id
	private Integer skuImgId;
	
	private Integer skuId;
	
	private Integer skuImgPrio;
	
	private String skuImgSrc;

	public Integer getSkuImgId() {
		return skuImgId;
	}

	public void setSkuImgId(Integer skuImgId) {
		this.skuImgId = skuImgId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getSkuImgPrio() {
		return skuImgPrio;
	}

	public void setSkuImgPrio(Integer skuImgPrio) {
		this.skuImgPrio = skuImgPrio;
	}

	public String getSkuImgSrc() {
		return skuImgSrc;
	}

	public void setSkuImgSrc(String skuImgSrc) {
		this.skuImgSrc = skuImgSrc;
	}
	
}
