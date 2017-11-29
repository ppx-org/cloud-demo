package com.ppx.cloud.grant.bean;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Column;
import com.ppx.cloud.common.jdbc.annotation.Id;

public class MerchantAccount {

	@Id
	private Integer accountId;

	private Integer merchantId;

	private String loginAccount;

	private String loginPassword;

	@Column(readonly = true)
	private String merchantName;

	@Column(readonly = true)
	private Date created;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
