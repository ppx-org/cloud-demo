package com.ppx.cloud.store.product.change;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public String getSkuMsg(Integer skuId) {
		String sql = "select ifnull((select concat(SKU_NAME, ':', STOCK_NUM) from sku where SKU_ID = ?), '')";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, skuId);
		return msg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional
	public int addStockChange(StockChange stockChange) {
		insert(stockChange);
		
		// 变更库存
		String updateSql = "update sku set STOCK_NUM = STOCK_NUM + ? where SKU_ID = ?";
		int r = getJdbcTemplate().update(updateSql, stockChange.getChangeNum(), stockChange.getSkuId());
		
		String getSql = "select STOCK_NUM from sku where SKU_ID = ?";
		int stock = getJdbcTemplate().queryForObject(getSql, Integer.class, stockChange.getSkuId());
		
		return stock;
	}
	
	

	
	
	
}
