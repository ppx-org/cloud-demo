package com.ppx.cloud.store.product.release.bean;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class ProductDetail {
	
	@Id
	private Integer prodId;
	
	private Date created;
	
	private Integer creator;
	
	private String prodPosition;
	
	private String barCode;
	
	private String prodDesc;
	
	private String prodArgs;

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
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

	public String getProdPosition() {
		return prodPosition;
	}

	public void setProdPosition(String prodPosition) {
		this.prodPosition = prodPosition;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public String getProdArgs() {
		return prodArgs;
	}

	public void setProdArgs(String prodArgs) {
		this.prodArgs = prodArgs;
	}
	
	
	
}
