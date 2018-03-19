package com.ppx.cloud.store.promo.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;

@Service
public class PromoCodeService extends MyDaoSupport {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	
	public void insert() {
		/*
		String code = "AB_CODE";
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("date", new Date());
		mongoTemplate.insert(map, "data_promo_code_detail");
		*/
		
	}
	
	
	public void test() {
		String code = "AB_CODE";
		String name = "TEST_NAME";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("_id", code);
		map.put("name", name);
		mongoTemplate.insert(map, "data_promo_code");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
