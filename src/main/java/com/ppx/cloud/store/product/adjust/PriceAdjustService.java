package com.ppx.cloud.store.product.adjust;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class PriceAdjustService extends MyDaoSupport {
	
	public PageList<PriceAdjust> listPriceAdjust(Page page, Integer skuId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(skuId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from stock_change c where c.SKU_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from stock_change c where c.SKU_ID = ?");
		
		List<PriceAdjust> list = queryPage(PriceAdjust.class, page, cSql, qSql, paraList);
		
		return new PageList<PriceAdjust>(list, page);
	}
	
	
	
	
	public String getSkuMsg(Integer skuId) {
		String sql = "select concat(SKU_NAME, ':', PRICE) from sku where SKU_ID = ?";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, skuId);

		return msg;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional
	public int addPriceAdjust(PriceAdjust priceAdjust) {
		insert(priceAdjust);
		
		// 变更库存
		String updateSql = "update sku set PRICE = ? where SKU_ID = ?";
		int r = getJdbcTemplate().update(updateSql, priceAdjust.getAdjustPrice(), priceAdjust.getSkuId());
		
		String getSql = "select STOCK_NUM from sku where SKU_ID = ?";
		int stock = getJdbcTemplate().queryForObject(getSql, Integer.class, priceAdjust.getSkuId());
		
		return stock;
	}
	
	

	
	
	
}
