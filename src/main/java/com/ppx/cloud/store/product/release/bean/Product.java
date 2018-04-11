package com.ppx.cloud.store.product.release.bean;

import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.annotation.Column;
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
	
	private String mainImgSrc;
	
	@Column(readonly=true)
	private String prodPrice;
	
	@Column(readonly=true)
	private Integer prodStock;
	
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
	
	public String getProdStatusDesc() {
		return Dict.getProdStatusDesc(prodStatus);
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

	public String getProdPrice() {
		if (!StringUtils.isEmpty(prodPrice)) {
			String[] price = prodPrice.split("-");
			if (price[0].equals(price[1])) {
				return price[0];
			}			
		}
		return prodPrice;
	}

	public void setProdPrice(String prodPrice) {
		this.prodPrice = prodPrice;
	}

	public Integer getProdStock() {
		return prodStock;
	}

	public void setProdStock(Integer prodStock) {
		this.prodStock = prodStock;
	}

	public String getMainImgSrc() {
		return mainImgSrc;
	}

	public void setMainImgSrc(String mainImgSrc) {
		this.mainImgSrc = mainImgSrc;
	}

	
	
}
	
	
	

