package com.ppx.cloud.micro.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MSearchService extends MyDaoSupport {
	
	
	public List<String> listLastWord() {
		
		String openid = MGrantContext.getWxUser().getOpenid();
		String sql = "select LAST_WORD from search_last_word where OPENID = ? order by CREATED desc";
		List<String> list = getJdbcTemplate().queryForList(sql, String.class, openid);
		
		return list;
	}
	
	public int deleteLastWord() {
		String openid = MGrantContext.getWxUser().getOpenid();
		String sql = "delete from search_last_word where OPENID = ?";
		int r = getJdbcTemplate().update(sql, openid);
		return r;
	}
	
	
	
	public List<String> listHotWord() {
		
		int storeId = MGrantContext.getWxUser().getStoreId();
		String sql = "select HOT_WORD from search_hot_word where STORE_ID = ? order by HOT_PRIO";
		List<String> list = getJdbcTemplate().queryForList(sql, String.class, storeId);
		
		return list;
	}
	
	public String getKeyWord() {
		
		Integer storeId = MGrantContext.getWxUser().getStoreId();
		String sql = "select ifnull((select KEY_WORD from search_key_word where STORE_ID = ?), '')";
		String r = getJdbcTemplate().queryForObject(sql, String.class, storeId);
		
		return r;
	}
	
	
	
	
	
	
	
}
