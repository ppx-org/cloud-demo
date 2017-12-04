package com.ppx.cloud.store.merchant.repo;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;

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
	
	public int insertRepository(Repository bean) {
		return insert(bean);
	}
	
	public Repository getRepository(Integer id) {
		Repository bean = getJdbcTemplate().queryForObject("select * from repository where CAT_ID = ?",
				BeanPropertyRowMapper.newInstance(Repository.class), id);		
		return bean;
	}
	
	public int updateRepository(Repository bean) {
		return update(bean);
	}
	
	public int deleteRepository(Integer id) {
		return getJdbcTemplate().update("update repository set RECORD_STATUS = ? where CAT_ID = ?", 0, id);
	}
}
