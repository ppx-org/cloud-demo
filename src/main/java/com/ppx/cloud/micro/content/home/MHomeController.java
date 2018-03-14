package com.ppx.cloud.micro.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.cache.RedisConfig;
import com.ppx.cloud.demo.common.query.QueryProduct;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	@Autowired
	private CacheManager cacheManager;
	
	@PostMapping @ResponseBody
	public Map<String, Object> test() {
		
		
		// Collection<String> c = cacheManager.getCacheNames();
	
		
		// RedisCacheManager rCache = (RedisCacheManager) cacheManager;
		
		Cache cache = cacheManager.getCache("listHome");
		
		ObjectMapper om = new ObjectMapper();
		
		try {
			System.out.println("-------------------0:" + om.writer().writeValueAsString(cache.get("SimpleKey []").get()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return ControllerReturn.ok();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Cacheable(value = "listHome")
	@PostMapping @ResponseBody
	public Map<String, Object> listHome() {
		List<MSwiper> swiperList = serv.listSwiper();
		List<MLevel> levelList = serv.listLevel();
		MPage page = new MPage();
		List<QueryProduct> prodList = serv.listLevelProd(page);
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("swiperList", swiperList);
		returnMap.put("levelList", levelList);
		returnMap.put("prodList", prodList);
		returnMap.put("page", page);
		
		return returnMap;
	}
	
	// 更多时调用
	@Cacheable(value = "levelProd", keyGenerator = RedisConfig.WISELY_KEY_GENERATOR)
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProd(@RequestBody MPage page) {
		List<QueryProduct> list = serv.listLevelProd(page);
		return ControllerReturn.ok(list);
	}
	
	
	
	
}

