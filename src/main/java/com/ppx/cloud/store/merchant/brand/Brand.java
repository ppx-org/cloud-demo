package com.ppx.cloud.store.merchant.brand;

import com.ppx.cloud.common.jdbc.annotation.Id;


public class Brand {
	
	@Id
	private Integer brandId;
	
	private Integer merchantId;
	
	private String brandName;
	
	private Integer brandPrio;
	
	private Integer recordStatus;
	
	private Integer brandImgX;
	
	private Integer brandImgY;

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	
	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getBrandPrio() {
		return brandPrio;
	}

	public void setBrandPrio(Integer brandPrio) {
		this.brandPrio = brandPrio;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getBrandImgX() {
		return brandImgX;
	}

	public void setBrandImgX(Integer brandImgX) {
		this.brandImgX = brandImgX;
	}

	public Integer getBrandImgY() {
		return brandImgY;
	}

	public void setBrandImgY(Integer brandImgY) {
		this.brandImgY = brandImgY;
	}
	
	
	
	

}
