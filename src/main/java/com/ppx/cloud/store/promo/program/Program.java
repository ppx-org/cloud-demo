package com.ppx.cloud.store.promo.program;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.store.common.dictionary.Dict;
import com.ppx.cloud.storecommon.price.utils.DecodePolicy;

public class Program {

	@Id
	private Integer progId;

	private Integer merchantId;

	private String progName;

	private Integer progPrio;

	@JsonFormat(pattern=DateUtils.DATE_PATTERN, timezone="GMT+8")
	@DateTimeFormat(pattern=DateUtils.DATE_PATTERN)
	private Date progBegin;

	@DateTimeFormat(pattern=DateUtils.DATE_PATTERN)
	private Date progEnd;

	private String policyType;
	
	private String policyArgs;
	
	private Date created;
	
	private Integer recordStatus;
	
	private Integer progImgX;

	private Integer progImgY;

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
	
	public String getProgEndDesc() {
		return DateUtils.getMaxDateDesc(progEnd);
	}

	public void setProgEnd(Date progEnd) {
		if (progEnd == null) {
			try {
				progEnd = new SimpleDateFormat(DateUtils.DATE_PATTERN).parse(DateUtils.MAX_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.progEnd = progEnd;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicyArgs() {
		return policyArgs;
	}
	
	public String getPolicyArgsDesc() {
		return DecodePolicy.decode(this.policyArgs);
	}

	public void setPolicyArgs(String policyArgs) {
		this.policyArgs = policyArgs;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getRecordStatusDesc() {
		return Dict.getProgramStatusDesc(this.recordStatus);
	}

	public Integer getProgImgX() {
		return progImgX;
	}

	public void setProgImgX(Integer progImgX) {
		this.progImgX = progImgX;
	}

	public Integer getProgImgY() {
		return progImgY;
	}

	public void setProgImgY(Integer progImgY) {
		this.progImgY = progImgY;
	}
	
	

}
