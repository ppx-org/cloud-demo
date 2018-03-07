package com.ppx.cloud.store.search.version;

import java.util.Date;

public class SearchLastUpdated {
	
	private int merchantId;

	private Date progLastUpdated;
	
	private Date prodLastUpdated;
	
	private Date prodPrioLastUpdated;

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
	
	public Date getProdPrioLastUpdated() {
		return prodPrioLastUpdated;
	}

	public void setProdPrioLastUpdated(Date prodPrioLastUpdated) {
		this.prodPrioLastUpdated = prodPrioLastUpdated;
	}

	public long getProgLastUpdatedView() {
		if (progLastUpdated != null) {
			return (new Date().getTime() - progLastUpdated.getTime()) / 1000 / 60;
		}
		return 0;
	}
	
	public long getProdLastUpdatedView() {
		if (prodLastUpdated != null) {
			return (new Date().getTime() - prodLastUpdated.getTime()) / 1000 / 60;
		}
		return 0;
	}
	
	public long getProdPrioLastUpdatedView() {
		if (prodPrioLastUpdated != null) {
			return (new Date().getTime() - prodPrioLastUpdated.getTime()) / 1000 / 60;
		}
		return 0;
	}
	
	
}
