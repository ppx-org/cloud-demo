package com.ppx.cloud.store.product.change;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class StockChangeService extends MyDaoSupport {
	
	public PageList<StockChange> listStockChange(Page page, Integer skuId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(skuId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from stock_change c where c.SKU_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from stock_change c where c.SKU_ID = ?");
		
		List<StockChange> list = queryPage(StockChange.class, page, cSql, qSql, paraList);
		
		
		
		return new PageList<StockChange>(list, page);
		
	}
	
	
	
	
	

	
	
	
}
