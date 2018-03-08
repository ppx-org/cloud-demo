package com.ppx.cloud.search.show.category;

import java.util.List;

public class SCategory {
	
	private Integer cid;
	
	private Integer pid;
	
	private String cn;
	
	private Integer n;
	
	private Integer x;
	
	private Integer y;
	
	private List<SCategory> children;

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

	public List<SCategory> getChildren() {
		return children;
	}

	public void setChildren(List<SCategory> children) {
		this.children = children;
	}

	public Integer getN() {
		return n;
	}

	public void setN(Integer n) {
		this.n = n;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
	
	
	
	
	
	
}
