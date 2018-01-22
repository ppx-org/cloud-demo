package com.ppx.cloud.search.show.category;

import java.util.List;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class MCategory {
	
	private Integer cid;
	
	private Integer pid;
	
	private String cn;
	
	private Integer n;
	
	private Integer x;
	
	private Integer y;
	
	private List<MCategory> children;

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

	public List<MCategory> getChildren() {
		return children;
	}

	public void setChildren(List<MCategory> children) {
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
