package com.ppx.cloud.search.version;

import java.util.Date;

public class SearchLastUpdated {
	
	private int merchantId;

	private Date progLastUpdated;
	
	private Date prodLastUpdated;

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public Date getProgLastUpdated() {
		return progLastUpdated;
	}

	public void setProgLastUpdated(Date progLastUpdated) {
		this.progLastUpdated = progLastUpdated;
	}

	public Date getProdLastUpdated() {
		return prodLastUpdated;
	}

	public void setProdLastUpdated(Date prodLastUpdated) {
		this.prodLastUpdated = prodLastUpdated;
	}
	
	
	
}
