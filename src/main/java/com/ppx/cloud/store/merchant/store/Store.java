package com.ppx.cloud.store.merchant.store;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;

@Table(name = "store")
public class Store {

	@Id
	private Integer storeId;
	
	private Integer merchantId;

	private String storeName;
	
	private String storeNo;
	
	private String storeLng;
	
	private String storeLat;
	
	private String storePhone;
	
	private String storeImg;
	
	private String storeAddress;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	
	public String getStoreLng() {
		return storeLng;
	}

	public void setStoreLng(String storeLng) {
		this.storeLng = storeLng;
	}

	public String getStoreLat() {
		return storeLat;
	}

	public void setStoreLat(String storeLat) {
		this.storeLat = storeLat;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public String getStoreImg() {
		return storeImg;
	}

	public void setStoreImg(String storeImg) {
		this.storeImg = storeImg;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	
	
}
