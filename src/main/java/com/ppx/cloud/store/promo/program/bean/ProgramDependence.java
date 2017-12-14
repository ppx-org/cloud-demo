package com.ppx.cloud.store.promo.program.bean;

public class ProgramDependence {

	private Integer progId;

	private Integer prodId;
	
	private Integer dependProdId;

	private Float dependPrice;
	
	private String prodTitle;

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public Integer getDependProdId() {
		return dependProdId;
	}

	public void setDependProdId(Integer dependProdId) {
		this.dependProdId = dependProdId;
	}

	public Float getDependPrice() {
		return dependPrice;
	}

	public void setDependPrice(Float dependPrice) {
		this.dependPrice = dependPrice;
	}

	public String getProdTitle() {
		return prodTitle;
	}

	public void setProdTitle(String prodTitle) {
		this.prodTitle = prodTitle;
	}

	

}
