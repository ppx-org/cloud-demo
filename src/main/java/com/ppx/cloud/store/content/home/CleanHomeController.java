package com.ppx.cloud.store.content.home;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class CleanHomeController {
	
	@Autowired
	private WebApplicationContext app;
	
	@Autowired
	private StoreService storeServ;

	@GetMapping
	public ModelAndView cleanHome() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> cleanListHome(@RequestParam Integer storeId) {
		StringRedisTemplate stringRedisTemplate = app.getBean(StringRedisTemplate.class);
		
		stringRedisTemplate.delete("listHome::" + storeId);
		
		Set<String> keys = stringRedisTemplate.keys("levelProd::" + storeId + "::*");
		stringRedisTemplate.delete(keys);
	
		return ControllerReturn.ok(1);
	}
}

