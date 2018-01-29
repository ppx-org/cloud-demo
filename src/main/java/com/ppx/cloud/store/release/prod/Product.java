package com.ppx.cloud.store.release.prod;

import com.ppx.cloud.store.common.dictionary.Dict;

public class Product {
	private Integer prodId;
	
	private Integer repoId;
	
	private Integer catId;
	
	private Integer mainCatId;
	
	private String prodTitle;
	
	private Integer prodStatus;
	
	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getRepoId() {
		return repoId;
	}

	public void setRepoId(Integer repoId) {
		this.repoId = repoId;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	
	public Integer getMainCatId() {
		return mainCatId;
	}

	public void setMainCatId(Integer mainCatId) {
		this.mainCatId = mainCatId;
	}

	public String getProdTitle() {
		return prodTitle;
	}

	public void setProdTitle(String prodTitle) {
		this.prodTitle = prodTitle;
	}
	
	public String getRecordStatusDesc() {
		return Dict.getProductStatusDesc(prodStatus);
	}

	public Integer getProdStatus() {
		return prodStatus;
	}

	public void setProdStatus(Integer prodStatus) {
		this.prodStatus = prodStatus;
	}


}
	
	
	

