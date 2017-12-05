package com.ppx.cloud.store.merchant.repo;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class RepositoryService extends MyDaoSupport {

	public PageList<Repository> listRepository(Page page, Repository bean) {
		MyCriteria c = createCriteria("where").addAnd("REPO_NAME like ?", "%", bean.getRepoName(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from repository where RECORD_STATUS = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from repository where RECORD_STATUS = ? order by REPO_ID desc").append(c);
		c.addPrePara(1);
		
		List<Repository> list = queryPage(Repository.class, page, cSql, qSql, c.getParaList());
		return new PageList<Repository>(list, page);
	}
	
	public List<Repository> listAllRepository() {
		String sql = "select REPO_ID, REPO_NAME from repository where RECORD_STATUS = ?";
		List<Repository> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Repository.class), 1);
		return list;
	}
	
	public List<Repository> listStoreRepo(Integer storeId) {
		String sql = "select REPO_ID, REPO_NAME from repository where RECORD_STATUS = ?";
		List<Repository> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Repository.class), 1);
		return list;
	}
	
	public int insertRepository(Repository bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		bean.setMerchantId(merchantId);
		return insert(bean);
	}
	
	public Repository getRepository(Integer id) {
		Repository bean = getJdbcTemplate().queryForObject("select * from repository where REPO_ID = ?",
				BeanPropertyRowMapper.newInstance(Repository.class), id);		
		return bean;
	}
	
	public int updateRepository(Repository bean) {
		return update(bean);
	}
	
	public int deleteRepository(Integer id) {
		return getJdbcTemplate().update("update repository set RECORD_STATUS = ? where REPO_ID = ?", 0, id);
	}
}
