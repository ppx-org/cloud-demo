package com.ppx.cloud.search.version;

import java.util.Date;

public class SearchVersion {
	
	private Integer merchantId;
	
	private String versionName;
	
	private Date createBegin;
	
	private Date createEnd;
	
	private Integer versionStatus;
	
	private String createInfo;
	
	

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Date getCreateBegin() {
		return createBegin;
	}

	public void setCreateBegin(Date createBegin) {
		this.createBegin = createBegin;
	}

	public Date getCreateEnd() {
		return createEnd;
	}

	public void setCreateEnd(Date createEnd) {
		this.createEnd = createEnd;
	}

	public Integer getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(Integer versionStatus) {
		this.versionStatus = versionStatus;
	}

	public String getCreateInfo() {
		return createInfo;
	}

	public void setCreateInfo(String createInfo) {
		this.createInfo = createInfo;
	}
	
	
	
}
