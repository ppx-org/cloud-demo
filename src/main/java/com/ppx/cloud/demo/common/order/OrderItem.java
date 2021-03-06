package com.ppx.cloud.demo.common.order;

import com.ppx.cloud.common.jdbc.annotation.Column;
import com.ppx.cloud.common.jdbc.annotation.Table;

@Table(name="order_item")
public class OrderItem {
	
	private Integer itemId;
	
	private Integer orderId;
	
	private Integer skuId;
	
	private Float itemUnitPrice;
	
	private Float itemPrice;
	
	private Integer itemNum;
	
	private String itemTitle;
	
	private String itemSku;
	
	private String itemImg;
	
	private String itemPromo;
	
	@Column(readonly=true)
	private Integer storeId;
	
	@Column(readonly=true)
	private Integer itemStatus;
	
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Float getItemUnitPrice() {
		return itemUnitPrice;
	}

	public void setItemUnitPrice(Float itemUnitPrice) {
		this.itemUnitPrice = itemUnitPrice;
	}

	public Float getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Float itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getItemNum() {
		return itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public String getItemImg() {
		return itemImg;
	}

	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}

	public String getItemPromo() {
		return itemPromo;
	}

	public void setItemPromo(String itemPromo) {
		this.itemPromo = itemPromo;
	}

	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
