package com.ppx.cloud.store.product.prio;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ProdPrioService extends MyDaoSupport {
	
	public PageList<ProdPrio> listProdPrio(Page page, ProdPrio bean) {
		
		// 默认排序，后面加上需要从页面传过来的排序的，防止SQL注入
		page.addDefaultOrderName("PROD_ID").addPermitOrderName("PROD_PRIO");
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		MyCriteria c = createCriteria("and").addAnd("PROD_ID = ?", bean.getProdId());
		c.addPrePara(merchantId);
		
		StringBuilder cSql = new StringBuilder("select count(*) from product where MERCHANT_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select PROD_ID, PROD_TITLE, PROD_PRIO from product where MERCHANT_ID = ?").append(c);
		
		List<ProdPrio> list = queryPage(ProdPrio.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProdPrio>(list, page);
	}
	
	public ProdPrio getProdPrio(Integer id) {
		ProdPrio bean = getJdbcTemplate().queryForObject("select * from product where PROD_ID = ?",
				BeanPropertyRowMapper.newInstance(ProdPrio.class), id);		
		return bean;
	}
	
	
	public int updateProdPrio(Integer prodId, Integer prodPrio) {
		String updateSql = "update product set PROD_PRIO = ? where PROD_ID = ?";
		return getJdbcTemplate().update(updateSql, prodPrio, prodId);
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
