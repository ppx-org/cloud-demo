package com.ppx.cloud.demo.module.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ExcelService extends MyDaoSupport {

	public List<Map<String, Object>> listData(int size) {
		String sql = "select TEST_ID, concat(TEST_NAME, TEST_ID, " + size +") TEST_NAME from test";
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
