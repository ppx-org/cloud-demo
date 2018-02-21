package com.ppx.cloud.store.merchant.store;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;
import com.ppx.cloud.common.util.DateUtils;

@Table(name = "store")
public class Store {

	@Id
	private Integer storeId;
	
	private Integer merchantId;

	private String storeName;
	
	private String storeNo;
	
	private String storeLng;
	
	private String storeLat;
	
	@JsonFormat(pattern=DateUtils.HHMM_PATTERN, timezone="GMT+8")
	@DateTimeFormat(pattern=DateUtils.HHMM_PATTERN)
	private Date businessBegin;
	
	@JsonFormat(pattern=DateUtils.HHMM_PATTERN, timezone="GMT+8")
	@DateTimeFormat(pattern=DateUtils.HHMM_PATTERN)
	private Date businessEnd;
	
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

	public Date getBusinessBegin() {
		return businessBegin;
	}

	public void setBusinessBegin(Date businessBegin) {
		this.businessBegin = businessBegin;
	}

	public Date getBusinessEnd() {
		return businessEnd;
	}

	public void setBusinessEnd(Date businessEnd) {
		this.businessEnd = businessEnd;
	}

	
	
}
