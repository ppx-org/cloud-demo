package com.ppx.cloud.store.common.dictionary;

public class DictBean {
	private int value;
	
	private String name;
	
	public DictBean() {
		
	}
	
	public DictBean(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
