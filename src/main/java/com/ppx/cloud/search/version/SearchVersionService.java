package com.ppx.cloud.search.version;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.search.util.BitSetUtils;

@Service
public class SearchVersionService extends MyDaoSupport {
	
	
	public List<SearchVersion> listVersion() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
	
		String countSql = "select count(*) from search_version where MERCHANT_ID = ?";
		int c = getJdbcTemplate().queryForObject(countSql, Integer.class, merchantId);
		
		if (c == 0) {
			String insertSql = "insert into search_version(MERCHANT_ID, VERSION_NAME) values(?, ?)";
			getJdbcTemplate().update(insertSql, merchantId, "v1");
			getJdbcTemplate().update(insertSql, merchantId, "v2");
		}
		
		String querySql = "select * from search_version where MERCHANT_ID = ?";
		List<SearchVersion> list = getJdbcTemplate().query(querySql, BeanPropertyRowMapper.newInstance(SearchVersion.class), merchantId);
		
		
		return list;
	}
	

	
	public void initVersion() {
		// 3:使用中
		String sql = "select MERCHANT_ID, VERSION_NAME from search_version where VERSION_STATUS = ?";
		List<SearchVersion> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SearchVersion.class), 3);
		for (SearchVersion v : list) {
			BitSetUtils.setVersionMap(v.getMerchantId(), v.getVersionName());
		}
	}
	
}
