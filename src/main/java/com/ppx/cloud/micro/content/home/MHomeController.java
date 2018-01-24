package com.ppx.cloud.micro.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.storecommon.page.QueryPage;
import com.ppx.cloud.storecommon.query.bean.QueryProduct;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	// 合成一个
	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {
		List<String> swiperList = serv.listSwiper();
		List<MLevel> levelList = serv.listLevel();
		QueryPage page = new QueryPage();
		List<QueryProduct> prodList = serv.listLevelProd(page);
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("swiperList", swiperList);
		returnMap.put("levelList", levelList);
		returnMap.put("prodList", prodList);
		returnMap.put("page", page);
		
		return returnMap;
	}

	
	// 需求单独出来，下拉更多接口
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProd(QueryPage page) {
		List<QueryProduct> list = serv.listLevelProd(page);
		return ControllerReturn.ok(list);
	}
	
	
	
	
}

