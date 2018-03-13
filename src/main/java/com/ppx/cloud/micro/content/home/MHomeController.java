package com.ppx.cloud.micro.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryProduct;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	@Cacheable(value = "homeCache2")
	@PostMapping @ResponseBody
	public Map<String, Object> test() {
		
		System.out.println("-------------------test------------mongodb-----------");
		
		
		return ControllerReturn.ok();
	}
	
	
	@Cacheable(value = "homeCache5", key="'key5'")
	@PostMapping @ResponseBody
	public Map<String, Object> test5() {
		System.out.println("-------------------test------------mongod-cache222222222222222222-----------");
		
		
		return ControllerReturn.ok();
	}
	
	@Cacheable(value = "homeCache6", key="'key6'")
	@PostMapping @ResponseBody
	public Map<String, Object> test6() {
		System.out.println("-------------------test------------mongod-cache666666666-----------");
		
		
		
		return ControllerReturn.ok();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Cacheable(value = "homeCache")
	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {
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
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProd(@RequestBody MPage page) {
		List<QueryProduct> list = serv.listLevelProd(page);
		return ControllerReturn.ok(list);
	}
	
	
	
	
}

