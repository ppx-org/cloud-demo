package com.ppx.cloud.store.search.hitword;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class HitWordService extends MyDaoSupport {
	
	
	public List<HitWord> listHotWord(Integer storeId) {
		String sql = "select * from search_history_word where STORE_ID = ? order by CREATED desc";
		List<HitWord> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(HitWord.class), storeId);
		return list;
	}
	
	
}
