package com.ppx.cloud.search.query.bean;

import java.util.List;

import com.ppx.cloud.storecommon.page.MPage;
import com.ppx.cloud.storecommon.query.bean.MQueryProduct;


public class QueryPageList {

	private List<MQueryProduct> prodList;
	
	private List<QueryCategory> catList;
	
	private List<QueryBrand> brandList;
	
	private List<QueryTheme> themeList;
	
	private List<QueryPromo> promoList;
	
	private Integer fastN = 0;
	
	private MPage queryPage = new MPage();
	
	public QueryPageList() {
		
	}
	
	public QueryPageList(MPage queryPage, List<MQueryProduct> prodList, List<QueryCategory> catList,
			List<QueryBrand> brandList, List<QueryTheme> themeList, List<QueryPromo> promoList, Integer fastN) {
		this.queryPage = queryPage;
		this.prodList = prodList;
		this.catList = catList;
		this.brandList = brandList;
		this.themeList = themeList;
		this.promoList = promoList;
		this.fastN = fastN;
	}
	
	
	public QueryPageList(MPage queryPage, List<MQueryProduct> prodList) {
		this.queryPage = queryPage;
		this.prodList = prodList;
	}
	
	

	public List<MQueryProduct> getProdList() {
		return prodList;
	}

	public void setProdList(List<MQueryProduct> prodList) {
		this.prodList = prodList;
	}

	public List<QueryCategory> getCatList() {
		return catList;
	}

	public void setCatList(List<QueryCategory> catList) {
		this.catList = catList;
	}

	public MPage getQueryPage() {
		return queryPage;
	}

	public void setQueryPage(MPage queryPage) {
		this.queryPage = queryPage;
	}

	public List<QueryPromo> getPromoList() {
		return promoList;
	}

	public void setPromoList(List<QueryPromo> promoList) {
		this.promoList = promoList;
	}
	

	public List<QueryBrand> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<QueryBrand> brandList) {
		this.brandList = brandList;
	}

	public List<QueryTheme> getThemeList() {
		return themeList;
	}

	public void setThemeList(List<QueryTheme> themeList) {
		this.themeList = themeList;
	}

	public int getFastN() {
		return fastN;
	}

	public void setFastN(int fastN) {
		this.fastN = fastN;
	}

	
	
	

	
	
	
	
}
