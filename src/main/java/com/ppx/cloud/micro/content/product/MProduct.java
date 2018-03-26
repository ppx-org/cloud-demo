package com.ppx.cloud.micro.content.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ppx.cloud.demo.common.price.utils.DecodePolicy;

public class MProduct {
	
	private Integer prodId;
	
	private String prodTitle;
	
	private String prodPrice;
	
	private String policy;
	
	private String prodArgs;
	
	private String prodDesc;
	
	private List<String> imgSrcList = new ArrayList<String>();
	
	private List<MSku> skuList = new ArrayList<MSku>();
	
	
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
		//return policy;
	}

	public void setPolicy(String policy) {
		
		this.policy = policy;
	}

	public String getProdArgs() {
		return prodArgs;
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
	

	
	
}
