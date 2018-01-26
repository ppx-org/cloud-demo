package com.ppx.cloud.micro.user.order.bean;

import java.util.ArrayList;
import java.util.List;

public class ConfirmReturn {
	// 0成功, 1:数量超出
	private int result = 0;
		
	private List<OverflowSku> overflowList = new ArrayList<OverflowSku>();

	public void addOverflowSku(OverflowSku overflowSku) {
		overflowList.add(overflowSku);
	}
	
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<OverflowSku> getOverflowList() {
		return overflowList;
	}

	public void setOverflowList(List<OverflowSku> overflowList) {
		this.overflowList = overflowList;
	}
	
	
	
}
