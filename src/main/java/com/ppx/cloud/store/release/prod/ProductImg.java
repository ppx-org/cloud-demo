package com.ppx.cloud.store.release.prod;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class ProductImg {
	@Id
	private Integer prodImgId;
	
	private Integer prodId;
	
	private Integer prodImgPrio;
	
	private String prodImgSrc;

	public Integer getProdImgId() {
		return prodImgId;
	}

	public void setProdImgId(Integer prodImgId) {
		this.prodImgId = prodImgId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getProdImgPrio() {
		return prodImgPrio;
	}

	public void setProdImgPrio(Integer prodImgPrio) {
		this.prodImgPrio = prodImgPrio;
	}

	public String getProdImgSrc() {
		return prodImgSrc;
	}

	public void setProdImgSrc(String prodImgSrc) {
		this.prodImgSrc = prodImgSrc;
	}

	
	
}
