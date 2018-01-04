package com.ppx.cloud.store.order.bean;

import java.util.Date;

public class UserOrder {
	
	private Integer orderId;
	
	private Integer userId;
	
	private Integer storeId;
	
	private Date orderTime;
	
	private Integer orderStatus;
	
	private Float orderPrice;
	
	private Float payPrice;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Float getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Float payPrice) {
		this.payPrice = payPrice;
	}
	
	
	
	
}
