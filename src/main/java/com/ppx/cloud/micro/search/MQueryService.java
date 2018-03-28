package com.ppx.cloud.micro.search;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MQueryService extends MyDaoSupport {
	
	
	public String getImgSrc(String type) {
		int merId = MGrantContext.getWxUser().getMerId();
		String sql = "select ifnull(IMG_SRC, '-1') from home_img where MERCHANT_ID = ? and IMG_TYPE = ? union select -1 limit 1";
		String r = getJdbcTemplate().queryForObject(sql, String.class, merId, type);
		return r;
	}
	
	
	
	
}
