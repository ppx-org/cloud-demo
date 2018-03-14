package com.ppx.cloud.demo.module.test;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;


@Service
public class ExcelService extends MyDaoSupport {

	public List<Map<String, Object>> listData(int size) {
		String sql = "select TEST_ID, concat(TEST_NAME, TEST_ID, " + size +") TEST_NAME from test";
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
