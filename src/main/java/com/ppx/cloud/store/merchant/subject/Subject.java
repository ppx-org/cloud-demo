package com.ppx.cloud.store.merchant.subject;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Subject {

	@Id
	private Integer subjectId;

	private Integer merchantId;

	private String subjectName;

	private Integer subjectPrio;

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getSubjectPrio() {
		return subjectPrio;
	}

	public void setSubjectPrio(Integer subjectPrio) {
		this.subjectPrio = subjectPrio;
	}

}
