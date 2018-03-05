package com.ppx.cloud.store.product.prio;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.demo.module.test.TestBean;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ProdPrioService extends MyDaoSupport {
	
	public PageList<ProdPrio> listProdPrio(Page page, ProdPrio bean) {
		MyCriteria c = createCriteria("and").addAnd("PROD_ID like ?", "%", bean.getProdId(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from product where MERCHANT_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from product where MERCHANT_ID = ?").append(c);
		
		List<ProdPrio> list = queryPage(ProdPrio.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProdPrio>(list, page);
	}
	
	public ProdPrio getProdPrio(Integer id) {
		ProdPrio bean = getJdbcTemplate().queryForObject("select * from product where PROD_ID = ?",
				BeanPropertyRowMapper.newInstance(ProdPrio.class), id);		
		return bean;
	}
	
	
	public int updateProdPrio(ProdPrio bean) {
		return update(bean);
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
