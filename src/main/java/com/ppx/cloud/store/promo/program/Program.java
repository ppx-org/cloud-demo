package com.ppx.cloud.store.promo.program;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.InitBinder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.util.DateUtils;

public class Program {

	@Id
	private Integer progId;

	private Integer merchantId;

	private String progName;

	private Integer progPrio;

	@JsonFormat(pattern=DateUtils.DATE_PATTERN)
	@DateTimeFormat(pattern=DateUtils.DATE_PATTERN)
	private Date progBegin;

	@JsonFormat(pattern=DateUtils.DATE_PATTERN)
	@DateTimeFormat(pattern=DateUtils.DATE_PATTERN)
	private Date progEnd;

	private String policy;

	private String policyArgs;

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getProgName() {
		return progName;
	}

	public void setProgName(String progName) {
		this.progName = progName;
	}

	public Integer getProgPrio() {
		return progPrio;
	}

	public void setProgPrio(Integer progPrio) {
		this.progPrio = progPrio;
	}

	public Date getProgBegin() {
		return progBegin;
	}

	public void setProgBegin(Date progBegin) {
		this.progBegin = progBegin;
	}

	public Date getProgEnd() {
		return progEnd;
	}

	public void setProgEnd(Date progEnd) {
		this.progEnd = progEnd;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPolicyArgs() {
		return policyArgs;
	}

	public void setPolicyArgs(String policyArgs) {
		this.policyArgs = policyArgs;
	}

}
