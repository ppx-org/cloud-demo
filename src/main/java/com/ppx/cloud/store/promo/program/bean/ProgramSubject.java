package com.ppx.cloud.store.promo.program.bean;

import com.ppx.cloud.store.promo.util.PolicyUtils;

public class ProgramSubject {
	
	private Integer progId;

	private Integer subjectId;

	private String subjectPolicy;

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectPolicy() {
		subjectPolicy = PolicyUtils.decodePolicy(subjectPolicy);
		return subjectPolicy;
	}

	public void setSubjectPolicy(String subjectPolicy) {
		this.subjectPolicy = subjectPolicy;
	}
	
	

	

}
