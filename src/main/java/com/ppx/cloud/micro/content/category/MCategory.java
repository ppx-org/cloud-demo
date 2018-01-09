package com.ppx.cloud.micro.content.category;

import java.util.List;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class MCategory {
	
	private Integer cid;
	
	private Integer pid;
	
	private String cn;
	
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
	
	
	
	
	
	
}
