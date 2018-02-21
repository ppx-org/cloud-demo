package com.ppx.cloud.store.product.changestatus;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.store.common.dictionary.Dict;

public class ChangeStatus {
	
	@Id
	private Integer changeStatusId;
	
	private Integer prodId;
	
	private Integer changeStatus;
	
	private Date created;
	
	private Integer creator;

	public Integer getChangeStatusId() {
		return changeStatusId;
	}

	public void setChangeStatusId(Integer changeStatusId) {
		this.changeStatusId = changeStatusId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getChangeStatus() {
		return changeStatus;
	}
	
	public String getChangeStatusDesc() {
		return Dict.getProdStatusDesc(changeStatus);
	}

	public void setChangeStatus(Integer changeStatus) {
		this.changeStatus = changeStatus;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	

}
	
	
	

