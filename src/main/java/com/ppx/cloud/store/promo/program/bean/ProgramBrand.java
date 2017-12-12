package com.ppx.cloud.store.promo.program.bean;

import com.ppx.cloud.store.promo.util.PolicyUtils;

public class ProgramBrand {
	private Integer progId;

	private Integer brandId;

	private String brandPolicy;

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandPolicy() {
		brandPolicy = PolicyUtils.decodePolicy(brandPolicy);
		return brandPolicy;
	}

	public void setBrandPolicy(String brandPolicy) {
		this.brandPolicy = brandPolicy;
	}

	

}
