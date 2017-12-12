package com.ppx.cloud.store.promo.program.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.ppx.cloud.common.jdbc.annotation.Column;
import com.ppx.cloud.store.promo.util.PolicyUtils;

public class ProgramSubject {
	
	private Integer progId;

	private Integer subjectId;

	private String subjectPolicy;
	
	@Column(readonly=true)
	private String subjectPolicyDesc;

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
		return subjectPolicy;
	}

	public void setSubjectPolicy(String subjectPolicy) throws UnsupportedEncodingException {
		subjectPolicy = URLDecoder.decode(subjectPolicy, "UTF-8");
		this.subjectPolicy = subjectPolicy;
	}

	public String getSubjectPolicyDesc() {
		subjectPolicyDesc = PolicyUtils.decodePolicy(subjectPolicy);
		return subjectPolicyDesc;
	}

	public void setSubjectPolicyDesc(String subjectPolicyDesc) {
		this.subjectPolicyDesc = subjectPolicyDesc;
	}
	
	

	

}
