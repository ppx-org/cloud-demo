package com.ppx.cloud.micro.content.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ppx.cloud.demo.common.price.utils.DecodePolicy;

public class MProduct {
	
	private Integer prodId;
	
	private String prodTitle;
	
	private String prodPrice;
	
	private String policy;
	
	private String prodArgs;
	
	private List<Map<String, String>> prodArg;
	
	private String prodDesc;
	
	private List<String> imgSrcList = new ArrayList<String>();
	
	private String skuDesc;
	
	private List<MSku> skuList = new ArrayList<MSku>();
	
	private Integer fast;
	
	
	public void setImgSrcStr(String imgSrcStr) {
		if (!StringUtils.isEmpty(imgSrcStr)) {
			String[] src = imgSrcStr.split(",");
			for (int i = 0; i < src.length; i++) {
				imgSrcList.add(src[i]);
			}
		}
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getProdTitle() {
		return prodTitle;
	}

	public void setProdTitle(String prodTitle) {
		this.prodTitle = prodTitle;
	}

	public List<String> getImgSrcList() {
		return imgSrcList;
	}

	public void setImgSrcList(List<String> imgSrcList) {
		this.imgSrcList = imgSrcList;
	}

	public String getProdPrice() {
		if (!StringUtils.isEmpty(prodPrice)) {
			String[] price = prodPrice.split("-");
			if (price[0].equals(price[1])) {
				return price[0];
			}			
		}
		return prodPrice;
	}

	public void setProdPrice(String prodPrice) {
		this.prodPrice = prodPrice;
	}

	public List<MSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<MSku> skuList) {
		this.skuList = skuList;
	}
	
	public String getPolicy() {
		return DecodePolicy.decode(policy);
	}

	public void setPolicy(String policy) {
		
		this.policy = policy;
	}

	public String getProdArgs() {
		return null;
	}

	public void setProdArgs(String prodArgs) {
		this.prodArgs = prodArgs;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public Integer getFast() {
		return fast;
	}

	public void setFast(Integer fast) {
		this.fast = fast;
	}

	public List<Map<String, String>> getProdArg() {
		if (!StringUtils.isEmpty(prodArgs)) {
			prodArg = new ArrayList<Map<String, String>>();
			String[] args = prodArgs.split("@");
			for (String s : args) {
				String[] v = (s + "| ").split("\\|");
				Map<String, String> map = new HashMap<String, String>();
				map.put("n", v[0]);
				map.put("v", v[1]);
				prodArg.add(map);
			}
		}
		return prodArg;
	}

	public void setProdArg(List<Map<String, String>> prodArg) {
		this.prodArg = prodArg;
	}

	public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	} 
	
	
	
	

	
	
}
