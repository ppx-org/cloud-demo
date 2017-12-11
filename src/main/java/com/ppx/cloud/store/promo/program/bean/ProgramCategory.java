package com.ppx.cloud.store.promo.program.bean;

import com.ppx.cloud.store.promo.util.PolicyUtils;

public class ProgramCategory {
	private Integer progId;

	private Integer catId;

	private String catPolicy;

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getCatPolicy() {
		catPolicy = PolicyUtils.decodePolicy(catPolicy);
		return catPolicy;
	}

	public void setCatPolicy(String catPolicy) {
		this.catPolicy = catPolicy;
	}

}
