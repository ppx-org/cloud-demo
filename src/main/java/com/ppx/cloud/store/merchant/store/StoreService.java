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
		MyCriteria c = createCriteria("where").addAnd("REPO_NAME like ?", "%", bean.getRepoName(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from store where RECORD_STATUS = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from store where RECORD_STATUS = ? order by STORE_ID desc").append(c);
		c.addPrePara(1);
		
		List<Store> list = queryPage(Store.class, page, cSql, qSql, c.getParaList());
		return new PageList<Store>(list, page);
	}
	
	public int insertStore(Store bean) {
		return insert(bean);
	}
	
	public Store getStore(Integer id) {
		Store bean = getJdbcTemplate().queryForObject("select * from store where CAT_ID = ?",
				BeanPropertyRowMapper.newInstance(Store.class), id);		
		return bean;
	}
	
	public int updateStore(Store bean) {
		return update(bean);
	}
	
	public int deleteStore(Integer id) {
		return getJdbcTemplate().update("update store set RECORD_STATUS = ? where CAT_ID = ?", 0, id);
	}
}
