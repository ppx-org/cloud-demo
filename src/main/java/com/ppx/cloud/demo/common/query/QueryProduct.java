package com.ppx.cloud.demo.common.query;

import com.ppx.cloud.demo.common.price.utils.DecodePolicy;

public class QueryProduct {
	
	private Integer pid;
	
	private Float p;

	private String arg;
	
	private Integer gid;
	
	private String t;
	
	private String n;
	
	private String src;
	
	private Integer f;
	
	private String promo;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Float getP() {
		return p;
	}

	public void setP(Float p) {
		this.p = p;
	}

	public String getArg() {
		return arg;
	}
	
	public void setPromo(String promo) {
		this.promo = promo;
	}
	
	public String getPromo() {
		this.promo = DecodePolicy.decode(this.arg);
		return promo;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}
	
	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Integer getF() {
		return f;
	}

	public void setF(Integer f) {
		this.f = f;
	}
	
	
	

	
}

























