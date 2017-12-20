package com.ppx.cloud.store.search.extword;

import com.ppx.cloud.common.jdbc.annotation.Table;


@Table(name="SEARCH_EXT_WORD")
public class ExtWord {
	
	private Integer merchantId;
	
	private String extWord;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getExtWord() {
		return extWord;
	}

	public void setExtWord(String extWord) {
		this.extWord = extWord;
	}
	
	

	
	

}
