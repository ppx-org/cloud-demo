package com.ppx.cloud.store.search.version;

import java.util.Date;

import com.ppx.cloud.search.util.SearchDict;

public class SearchVersion {
	
	private Integer merchantId;
	
	private String versionName;
	
	private Date createBegin;
	
	private Date createEnd;
	
	private Integer versionStatus;
	
	private String createInfo;
	
	private Date updated;
	
	private Integer updator;

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

	public String getVersionStatusDesc() {
		return SearchDict.getVersionStatusDesc(versionStatus);
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Long getSpendTime() {
		if (createEnd != null && createBegin != null) {
			return (createEnd.getTime() - createBegin.getTime()) / 1000;
		}
		return null;
	}

	public Integer getUpdator() {
		return updator;
	}

	public void setUpdator(Integer updator) {
		this.updator = updator;
	}
	
	
}
