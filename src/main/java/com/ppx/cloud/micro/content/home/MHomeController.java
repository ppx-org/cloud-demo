package com.ppx.cloud.micro.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.storecommon.page.MPage;
import com.ppx.cloud.storecommon.query.bean.MQueryProduct;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	// 合成一个
	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {
		List<String> swiperList = serv.listSwiper();
		List<MLevel> levelList = serv.listLevel();
		MPage page = new MPage();
		List<MQueryProduct> prodList = serv.listLevelProd(page);
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("swiperList", swiperList);
		returnMap.put("levelList", levelList);
		returnMap.put("prodList", prodList);
		returnMap.put("page", page);
		
		return returnMap;
	}

	
	// 需求单独出来，下拉更多接口
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProd(MPage page) {
		List<MQueryProduct> list = serv.listLevelProd(page);
		return ControllerReturn.ok(list);
	}
	
	
	
	
}

