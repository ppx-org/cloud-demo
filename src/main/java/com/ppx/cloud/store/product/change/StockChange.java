package com.ppx.cloud.store.product.change;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.store.common.dictionary.Dict;

public class StockChange {
	
	@Id
	private Integer changeId;
	
	private Integer skuId;
	
	private Integer changeNum;
	
	private Integer changeType;

	public Integer getChangeId() {
		return changeId;
	}

	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	
	

	
	
}
	
	
	

