package com.ppx.cloud.store.product.release.bean;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.store.common.dictionary.Dict;

public class Product {
	@Id
	private Integer prodId;
	
	private Integer merchantId;
	
	private Integer repoId;
	
	private Integer catId;
	
	private Integer mainCatId;
	
	private Integer brandId;
	
	private String prodTitle;
	
	private String skuDesc;
	
	private Integer prodStatus;
	
	
	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
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

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
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

	public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

	
	
}
	
	
	

