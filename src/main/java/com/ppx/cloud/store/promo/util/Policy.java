package com.ppx.cloud.store.promo.util;

public class Policy {
	
	private String value;

	private String name;
	
	public Policy() {
		
	}
	
	public Policy(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
