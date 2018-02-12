package com.ppx.cloud.store.product.adjust;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class PriceAdjustService extends MyDaoSupport {
	
	public PageList<PriceAdjust> listPriceAdjust(Page page, Integer skuId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(skuId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from price_adjust c where c.SKU_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from price_adjust c where c.SKU_ID = ? order by CREATED desc");
		
		List<PriceAdjust> list = queryPage(PriceAdjust.class, page, cSql, qSql, paraList);
		
		return new PageList<PriceAdjust>(list, page);
	}
	
	
	public String getSkuMsg(Integer skuId) {
		String sql = "select ifnull((select concat(SKU_NAME, ':', PRICE) from sku where SKU_ID = ?), '')";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, skuId);

		return msg;
	}
	
	
	@Transactional
	public double addPriceAdjust(PriceAdjust priceAdjust) {
		int creator = GrantContext.getLoginAccount().getAccountId();
		
		priceAdjust.setCreator(creator);
		insert(priceAdjust);
		
		// 变更库存
		String updateSql = "update sku set PRICE = ? where SKU_ID = ?";
		getJdbcTemplate().update(updateSql, priceAdjust.getAdjustPrice(), priceAdjust.getSkuId());
		
		
		return priceAdjust.getAdjustPrice();
	}
	
	


}


