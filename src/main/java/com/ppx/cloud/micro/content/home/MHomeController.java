package com.ppx.cloud.micro.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryProduct;
import com.ppx.cloud.micro.content.store.MStore;
import com.ppx.cloud.micro.content.store.MStoreService;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	@Autowired
	private MStoreService mStoreServ;
	
	@PostMapping @ResponseBody
	public Map<String, Object> test() {
		
		return ControllerReturn.ok();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//@Cacheable(value="listHome", keyGenerator=RedisConfig.STORE_ID_GENERATOR)
	@PostMapping @ResponseBody
	public Map<String, Object> listHome() {
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		MStore store = mStoreServ.getStore();
		List<MSwiper> swiperList = serv.listSwiper();
		List<MLevel> levelList = serv.listLevel();
		MPage page = new MPage();
		List<QueryProduct> prodList = serv.listLevelProd(page);
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("store", store);
		returnMap.put("swiperList", swiperList);
		returnMap.put("levelList", levelList);
		returnMap.put("prodList", prodList);
		returnMap.put("mPage", page);
		
		return returnMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	// 更多时调用
	//@Cacheable(value = "levelProd", keyGenerator = RedisConfig.STORE_ID_WISELY_GENERATOR)
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProd(@RequestBody MPage page) {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<QueryProduct> list = serv.listLevelProd(page);
		return ControllerReturn.ok(list, page);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

