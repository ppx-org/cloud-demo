package com.ppx.cloud.search.query.bean;

import com.ppx.cloud.storecommon.query.bean.QueryPage;

public class MQueryBean {
	
	private QueryPage p = new QueryPage();
	
	private String w;
	
	private Integer cid;
	
	private Integer bid;
	
	private Integer tid;
	
	private Integer gid;
	
	private Integer fast;
	
	private Integer o;

	public QueryPage getP() {
		return p;
	}

	public void setP(QueryPage p) {
		this.p = p;
	}

	public String getW() {
		return w;
	}

	public void setW(String w) {
		this.w = w;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getFast() {
		return fast;
	}

	public void setFast(Integer fast) {
		this.fast = fast;
	}

	public Integer getO() {
		return o;
	}

	public void setO(Integer o) {
		this.o = o;
	}
	
	
	
	

}
