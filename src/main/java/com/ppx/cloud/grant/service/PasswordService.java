package com.ppx.cloud.grant.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.common.GrantUtils;

@Service
public class PasswordService extends MyDaoSupport {
	
	public int updatePassword(String oldP, String newP) {
		Integer accoutId = GrantContext.getLoginAccount().getAccountId();
		Map<String, Object> map = getJdbcTemplate().queryForMap("select LOGIN_PASSWORD from merchant_account where ACCOUNT_ID = ?", accoutId);
		String password = (String)map.get("LOGIN_PASSWORD");
		if (!password.equals(GrantUtils.getMD5Password(oldP))) {
			// 旧密码不正确
			return -1;
		}
		// 更新密码
		int r = getJdbcTemplate().update("update merchant_account set LOGIN_PASSWORD = ? where ACCOUNT_ID = ?",
			GrantUtils.getMD5Password(newP), accoutId);
		return r;
	}
	
		

}
