package com.ppx.cloud.search.query.bean;

public class QueryCategory {
	
	private Integer cid;
	
	private Integer pid;
	
	private String cn;
	
	private Integer n;
	
	public QueryCategory() {
		
	}
	
	public QueryCategory(Integer cid, Integer n) {
		this.cid = cid;
		this.n = n;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public Integer getN() {
		return n;
	}

	public void setN(Integer n) {
		this.n = n;
	}

	

	
	
	
	
}

























