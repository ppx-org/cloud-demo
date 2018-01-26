package com.ppx.cloud.micro.user.order;

import java.util.List;

import com.ppx.cloud.storecommon.price.bean.SkuIndex;


public class ConfirmOrderItem {
	
	private List<SkuIndex> skuIndexList;
	
	private int totalNum;
	
	private float totalPrice;
	
	public ConfirmOrderItem(List<SkuIndex> skuIndexList, int totalNum, float totalPrice) {
		this.skuIndexList = skuIndexList;
		this.totalNum = totalNum;
		this.totalPrice = totalPrice;
	}

	public List<SkuIndex> getSkuIndexList() {
		return skuIndexList;
	}

	public void setSkuIndexList(List<SkuIndex> skuIndexList) {
		this.skuIndexList = skuIndexList;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
		
}
