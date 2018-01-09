package com.ppx.cloud.micro.content.theme;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MThemedService extends MyDaoSupport {
	

	
	
	public List<MTheme> listTheme() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		
		String sql = "select THEME_ID TID, THEME_NAME TN from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		
		List<MTheme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MTheme.class), merchantId, 1);	
		
		
		return list;
	}
	
	
	
	
	
}
