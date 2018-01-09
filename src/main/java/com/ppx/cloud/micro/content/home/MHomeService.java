package com.ppx.cloud.micro.content.home;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MHomeService extends MyDaoSupport {
	
	
	public List<String> listSwiper() {
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select SWIPER_IMG from home_swiper order by SWIPER_PRIO";
		List<String> list = getJdbcTemplate().queryForList(sql, String.class, storeId);
		
		return list;
	}
	
	
	
	
	
}
