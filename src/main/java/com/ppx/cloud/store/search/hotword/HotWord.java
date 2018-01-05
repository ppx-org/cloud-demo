package com.ppx.cloud.store.search.hotword;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;


@Table(name="SEARCH_HOT_WORD")
public class HotWord {
	
	@Id
	private Integer hotId;
	
	private Integer storeId;
	
	private String hotWord;
	
	private Integer hotPrio;

	public Integer getHotId() {
		return hotId;
	}

	public void setHotId(Integer hotId) {
		this.hotId = hotId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getHotWord() {
		return hotWord;
	}

	public void setHotWord(String hotWord) {
		this.hotWord = hotWord;
	}

	public Integer getHotPrio() {
		return hotPrio;
	}

	public void setHotPrio(Integer hotPrio) {
		this.hotPrio = hotPrio;
	}
	
	
	

	
	
	
	

	
	

}
