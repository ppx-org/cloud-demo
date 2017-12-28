package com.ppx.cloud.search.query.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author dengxz
 * @date 2017年11月14日
 */
public class QueryPageList {

	private List<QueryProduct> prodList = new ArrayList<QueryProduct>();
	
	private List<QueryCategory> catList = new ArrayList<QueryCategory>();
	
	private QueryPage queryPage = new QueryPage();
	
	public QueryPageList() {
		
	}
	
	public QueryPageList(List<QueryProduct> prodList, List<QueryCategory> catList, QueryPage queryPage) {
		this.prodList = prodList;
		this.catList = catList;
		this.queryPage = queryPage;
	}

	public List<QueryProduct> getProdList() {
		return prodList;
	}

	public void setProdList(List<QueryProduct> prodList) {
		this.prodList = prodList;
	}

	public List<QueryCategory> getCatList() {
		return catList;
	}

	public void setCatList(List<QueryCategory> catList) {
		this.catList = catList;
	}

	public QueryPage getQueryPage() {
		return queryPage;
	}

	public void setQueryPage(QueryPage queryPage) {
		this.queryPage = queryPage;
	}

	
	
	

	
	
	
	
}
