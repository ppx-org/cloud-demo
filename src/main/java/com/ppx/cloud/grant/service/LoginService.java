package com.ppx.cloud.grant.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantUtils;

@Service
public class LoginService extends MyDaoSupport {
	
	
	public Map<String, Object> getLoginAccount(String a, String p) {
		Map<String, Object> merchantMap = null;
		// admin超级管理员
		if ("admin".equals(a) && "admin".equals(p)) {
			merchantMap = new HashMap<String, Object>();
			merchantMap.put("ACCOUNT_ID", -1);
			merchantMap.put("LOGIN_ACCOUNT", a);
			merchantMap.put("MERCHANT_ID", -1);
			merchantMap.put("MERCHANT_NAME", "超级管理员");
			return merchantMap;
		}
		
		String countSql = "select count(*) from merchant_account where LOGIN_ACCOUNT = ? and LOGIN_PASSWORD = ? and RECORD_STATUS = ?";		
		int c = getJdbcTemplate().queryForObject(countSql, Integer.class, a, GrantUtils.getMD5Password(p), 1);
		if (c == 0) return null;
		
		String sql = "select a.ACCOUNT_ID, a.LOGIN_ACCOUNT, a.MERCHANT_ID, m.MERCHANT_NAME" + 
				" from merchant_account a left join merchant m on a.ACCOUNT_ID = m.MERCHANT_ID " +
				" where a.LOGIN_ACCOUNT = ? and a.LOGIN_PASSWORD = ?";
		merchantMap = getJdbcTemplate().queryForMap(sql, a, GrantUtils.getMD5Password(p));
		
		
		return merchantMap;
	}
	
	
}