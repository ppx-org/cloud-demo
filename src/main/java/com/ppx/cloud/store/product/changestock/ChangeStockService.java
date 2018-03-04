package com.ppx.cloud.store.product.changestock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ChangeStockService extends MyDaoSupport {
	
	public PageList<ChangeStock> listStockChange(Page page, Integer skuId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(skuId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from change_stock c where c.SKU_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from change_stock c where c.SKU_ID = ? order by CREATED desc");
		
		List<ChangeStock> list = queryPage(ChangeStock.class, page, cSql, qSql, paraList);
		return new PageList<ChangeStock>(list, page);
	}
	
	public String getSkuMsg(Integer skuId) {
		String sql = "select ifnull((select concat(ifnull(SKU_NAME, ''), ' 库存:', STOCK_NUM) from sku where SKU_ID = ?), '')";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, skuId);
		return msg;
	}
	

	
	@Transactional
	public int addChangeStock(ChangeStock stockChange) {
		int creator = GrantContext.getLoginAccount().getAccountId();
		stockChange.setCreator(creator);
		insert(stockChange);
		
		// 变更库存
		String updateSql = "update sku set STOCK_NUM = STOCK_NUM + ? where SKU_ID = ?";
		getJdbcTemplate().update(updateSql, stockChange.getChangeNum(), stockChange.getSkuId());
		
		String getSql = "select STOCK_NUM from sku where SKU_ID = ?";
		int stock = getJdbcTemplate().queryForObject(getSql, Integer.class, stockChange.getSkuId());
		
		return stock;
	}
	
	

	
	
	
}
