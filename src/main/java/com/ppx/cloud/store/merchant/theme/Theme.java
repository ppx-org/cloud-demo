package com.ppx.cloud.store.merchant.theme;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Theme {

	@Id
	private Integer ThemeId;

	private Integer merchantId;

	private String themeName;

	private Integer themePrio;

	public Integer getThemeId() {
		return ThemeId;
	}

	public void setThemeId(Integer themeId) {
		ThemeId = themeId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public Integer getThemePrio() {
		return themePrio;
	}

	public void setThemePrio(Integer themePrio) {
		this.themePrio = themePrio;
	}

	

	
}
