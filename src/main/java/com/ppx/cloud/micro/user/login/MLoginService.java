package com.ppx.cloud.micro.user.login;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.controller.ControllerContext;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.monitor.AccessLog;
import com.ppx.cloud.monitor.AccessUtils;


@Service
public class MLoginService extends MyDaoSupport {
	
	
	// 异步插入
	public void asynInsertUserInfo(String openid, int lastLoginType) {
		AccessLog log = new AccessLog();
		log.setUri("asynInsertUserInfo");
		log.setBeginTime(new Date());
		log.setIp("127.0.0.0");
		ControllerContext.setAccessLog(log);
		try {
			String insertSql = "insert into user_info(OPENID, LAST_LOGIN_UPDATED, LAST_LOGIN_TYPE) values(?, now(), ?)"
					+ " ON DUPLICATE KEY UPDATE LAST_LOGIN_UPDATED = now(), LAST_LOGIN_TYPE = ?"; 
			getJdbcTemplate().update(insertSql, openid, openid, lastLoginType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.setSpendTime(System.currentTimeMillis() - log.getBeginTime().getTime());
		AccessUtils.writeQueue(log);
	}
	
	
	
	
}
