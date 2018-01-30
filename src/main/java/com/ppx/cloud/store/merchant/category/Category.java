package com.ppx.cloud.store.merchant.category;

import java.util.List;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Category {
	
	@Id
	private Integer catId;
	
	private Integer merchantId;
	
	private Integer parentId;
	
	private String catName;
	
	private Integer catPrio;
	
	private Integer catImgX;
	
	private Integer catImgY;
	
	private Integer recordStatus;
	
	private List<Category> children;
	
	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Integer getCatPrio() {
		return catPrio;
	}

	public void setCatPrio(Integer catPrio) {
		this.catPrio = catPrio;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public Integer getCatImgX() {
		return catImgX;
	}

	public void setCatImgX(Integer catImgX) {
		this.catImgX = catImgX;
	}

	public Integer getCatImgY() {
		return catImgY;
	}

	public void setCatImgY(Integer catImgY) {
		this.catImgY = catImgY;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	
	
	
	
}
