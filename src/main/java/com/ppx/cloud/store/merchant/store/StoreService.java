package com.ppx.cloud.store.merchant.store;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class StoreService extends MyDaoSupport {

	public PageList<Store> listStore(Page page, Store bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("where").addAnd("s.STORE_NAME like ?", "%", bean.getStoreName(), "%");
		
		
		String sql = " where s.MERCHANT_ID = ? and s.RECORD_STATUS = ? ";
		StringBuilder cSql = new StringBuilder("select count(*) from store s").append(sql).append(c);
		StringBuilder qSql = new StringBuilder("select s.STORE_ID, s.STORE_NAME, s.STORE_NO, r.REPO_ADDRESS STORE_ADDRESS"
				+ " from store s left join repository r on s.STORE_ID = r.REPO_ID").append(sql).append("order by s.STORE_ID desc").append(c);
		c.addPrePara(merchantId);
		c.addPrePara(1);
		
		List<Store> list = queryPage(Store.class, page, cSql, qSql, c.getParaList());
		return new PageList<Store>(list, page);
	}
	

	@Transactional
	public int insertStore(Store bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		bean.setMerchantId(merchantId);
		int insertR = insert(bean, "STORE_ID");
		if (insertR == 0) return 0;
		
		int id = getLastInsertId();
		// 插入数据到关系表
		String sql = "insert into store_map_repo(REPO_ID, STORE_ID) values(?, ?)";
		int r = getJdbcTemplate().update(sql, id, id);
		
		return r;
	}
	
	public Store getStore(Integer id) {
		Store bean = getJdbcTemplate().queryForObject("select * from store where STORE_ID = ?",
				BeanPropertyRowMapper.newInstance(Store.class), id);		
		return bean;
	}
	
	public int updateStore(Store bean) {
		return update(bean);
	}
	
	public int deleteStore(Integer id) {
		return getJdbcTemplate().update("update store set RECORD_STATUS = ? where STORE_ID = ?", 0, id);
	}
	
	@Transactional
	public int saveStoreRepo(@RequestParam Integer id, @RequestParam Integer[] repoId) {
		String delSql = "delete from store_map_repo where STORE_ID = ?";
		getJdbcTemplate().update(delSql, id);
		
		String insertSql = "insert into store_map_repo(REPO_ID, STORE_ID) values(?, ?)";
		for (Integer rId : repoId) {
			getJdbcTemplate().update(insertSql, rId, id);
		}
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// >>>>>>>>>>>>其它地方调用
	public List<Store> listStore() {
		int merchantId = -1;//GrantContext.getLoginAccount().getMerchantId();
	
		String sql = "select STORE_ID, STORE_NAME from store where MERCHANT_ID = ? order by STORE_ID desc";
		
		List<Store> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Store.class), merchantId);
		
		return list;
	}
	
	
	
}
