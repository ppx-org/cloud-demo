package com.ppx.cloud.store.product.change;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class StockChangeService extends MyDaoSupport {
	
	public PageList<StockChange> listStockChange(Page page, Integer skuId) {
		
	
		StringBuilder cSql = new StringBuilder("select count(*) from stock_change c where SKU_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from stock_change c where SKU_ID = ?");
		
		List<StockChange> list = queryPage(StockChange.class, page, cSql, qSql);
		return new PageList<StockChange>(list, page);
		
	}
	
	
	
	
	

	
	
	
}
