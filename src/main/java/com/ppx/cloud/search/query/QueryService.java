package com.ppx.cloud.search.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;


@Service
public class QueryService extends MyDaoSupport {
	
	public int[] findProdId(String w) {
		
		
		int[] prodId = {1,3,5};		
		return prodId;
	}
	
	
	
	
	public List<QueryProduct> listProduct(int[] prodId) {
		
		
		
		
		return null;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
