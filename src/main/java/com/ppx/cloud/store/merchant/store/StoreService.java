package com.ppx.cloud.store.merchant.store;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;

@Service
public class StoreService extends MyDaoSupport {

	public PageList<Store> listStore(Page page, Store bean) {
		MyCriteria c = createCriteria("where").addAnd("s.STORE_NAME like ?", "%", bean.getStoreName(), "%");
		
		
		
		StringBuilder cSql = new StringBuilder("select count(*) from store s where s.RECORD_STATUS = ?").append(c);
		StringBuilder qSql = new StringBuilder("select s.STORE_ID, s.STORE_NAME, s.STORE_NO, r.REPO_ADDRESS STORE_ADDRESS"
				+ " from store s left join repository r on s.STORE_ID = r.REPO_ID where s.RECORD_STATUS = ? order by s.STORE_ID desc").append(c);
		c.addPrePara(1);
		
		List<Store> list = queryPage(Store.class, page, cSql, qSql, c.getParaList());
		return new PageList<Store>(list, page);
	}
	
	public int insertStore(Store bean) {
		return insert(bean);
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
}
