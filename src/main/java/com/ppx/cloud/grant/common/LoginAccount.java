package com.ppx.cloud.grant.common;

public class LoginAccount  {
	
    private Integer accountId;

    private String loginAccount;

	private Integer merchantId;
	
	private String merchantName;
	
	public boolean isMainAccont() {
		return accountId == merchantId;
	}
	
	public boolean isAdmin() {
		return accountId == -1 ? true : false;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
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

	
	

}