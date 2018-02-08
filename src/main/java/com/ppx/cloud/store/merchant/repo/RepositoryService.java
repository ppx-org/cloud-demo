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
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("and").addAnd("REPO_NAME like ?", "%", bean.getRepoName(), "%");
		
		String sql = " where MERCHANT_ID = ? and RECORD_STATUS = ? ";
		StringBuilder cSql = new StringBuilder("select count(*) from repository").append(sql).append(c);
		StringBuilder qSql = new StringBuilder("select * from repository").append(sql).append(c).append("order by REPO_ID desc");
		c.addPrePara(merchantId);
		c.addPrePara(1);
		
		List<Repository> list = queryPage(Repository.class, page, cSql, qSql, c.getParaList());
		return new PageList<Repository>(list, page);
	}
	
	public List<Repository> displayRepository() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select REPO_ID, REPO_NAME from repository where MERCHANT_ID = ? and RECORD_STATUS = ?";
		List<Repository> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Repository.class), merchantId, 1);
		return list;
	}
	
	public List<Repository> listStoreRepo(Integer storeId) {
		String sql = "select r.REPO_ID, r.REPO_NAME from repository r join store_map_repo m on r.REPO_ID = m.REPO_ID "
				+ "where m.STORE_ID = ? and RECORD_STATUS = ? order by r.REPO_ID";
		List<Repository> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Repository.class), storeId, 1);
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
