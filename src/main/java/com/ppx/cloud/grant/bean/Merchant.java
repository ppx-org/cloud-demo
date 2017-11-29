package com.ppx.cloud.grant.bean;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Column;
import com.ppx.cloud.common.jdbc.annotation.Id;

public class Merchant {
	
	@Id
	private Integer merchantId;
	
	private String merchantName;
	
	@Column(readonly=true)
	private Date created;
	
	@Column(readonly=true)
	private String loginAccount;
	
	@Column(readonly=true)
	private String loginPassword;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	

}
