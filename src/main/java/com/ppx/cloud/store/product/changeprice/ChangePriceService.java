package com.ppx.cloud.store.product.changeprice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ChangePriceService extends MyDaoSupport {
	
	public PageList<ChangePrice> listChangePrice(Page page, Integer skuId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(skuId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from change_price c where c.SKU_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from change_price c where c.SKU_ID = ? order by CREATED desc");
		
		List<ChangePrice> list = queryPage(ChangePrice.class, page, cSql, qSql, paraList);
		
		return new PageList<ChangePrice>(list, page);
	}
	
	
	public String getSkuMsg(Integer skuId) {
		String sql = "select ifnull((select concat(ifnull(SKU_NAME, ''), ' 价格:', PRICE) from sku where SKU_ID = ?), '')";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, skuId);

		return msg;
	}
	
	
	@Transactional
	public double addChangePrice(ChangePrice changePrice) {
		int creator = GrantContext.getLoginAccount().getAccountId();
		
		changePrice.setCreator(creator);
		insert(changePrice);
		
		// 变更库存
		String updateSql = "update sku set PRICE = ? where SKU_ID = ?";
		getJdbcTemplate().update(updateSql, changePrice.getChangePrice(), changePrice.getSkuId());
		
		
		return changePrice.getChangePrice();
	}
	
	


}


