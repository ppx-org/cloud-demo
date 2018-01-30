package com.ppx.cloud.store.merchant.theme;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Theme {

	@Id
	private Integer themeId;

	private Integer merchantId;

	private String themeName;

	private Integer themePrio;
	
	private Integer recordStatus;
	
	private Integer themeImgX;
	
	private Integer themeImgY;
	
	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
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

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getThemeImgX() {
		return themeImgX;
	}

	public void setThemeImgX(Integer themeImgX) {
		this.themeImgX = themeImgX;
	}

	public Integer getThemeImgY() {
		return themeImgY;
	}

	public void setThemeImgY(Integer themeImgY) {
		this.themeImgY = themeImgY;
	}
	
}
