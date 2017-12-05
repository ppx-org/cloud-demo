package com.ppx.cloud.store.merchant.repo;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;

@Table(name = "repository")
public class Repository {

	@Id
	private Integer repoId;

	private Integer merchantId;
	
	private String repoName;
	
	private String repoAddress;
	

	public Integer getRepoId() {
		return repoId;
	}

	public void setRepoId(Integer repoId) {
		this.repoId = repoId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoAddress() {
		return repoAddress;
	}

	public void setRepoAddress(String repoAddress) {
		this.repoAddress = repoAddress;
	}
	
	

}
