package com.ppx.cloud.search.query.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author dengxz
 * @date 2017年11月14日
 */
public class QueryPageList {

	private List<QueryProduct> list = new ArrayList<QueryProduct>();
	
	private QueryPage queryPage = new QueryPage();
	
	public QueryPageList() {
		
	}
	
	public QueryPageList(List<QueryProduct> list, QueryPage queryPage) {
		this.list = list;
		this.queryPage = queryPage;
	}

	public List<QueryProduct> getList() {
		return list;
	}

	public void setList(List<QueryProduct> list) {
		this.list = list;
	}

	public QueryPage getQueryPage() {
		return queryPage;
	}

	public void setQueryPage(QueryPage queryPage) {
		this.queryPage = queryPage;
	}

	
	
	
	
}
