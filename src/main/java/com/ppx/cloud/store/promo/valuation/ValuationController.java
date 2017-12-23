package com.ppx.cloud.store.promo.valuation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.demo.module.test.TestBean;


@Controller	
public class ValuationController {
	
	@Autowired
	private ValuationService serv;
	
	
	@GetMapping
	public ModelAndView testPrice() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("today", DateUtils.today());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSku(@RequestParam String date, @RequestParam String skuIdStr) {
		//serv.count(skuIndexMap)
		
		
		return ControllerReturn.ok();
	}
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> test2() {
		
	
		
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		
		SkuIndex s1 = new SkuIndex(1, 3);
		SkuIndex s2 = new SkuIndex(3, 5);
		skuIndexMap.put(1, s1);
		skuIndexMap.put(3, s2);
		
		int r = serv.count(skuIndexMap);
		
		System.out.println("==============r:" + r);
		
		return ControllerReturn.ok();
	}
	
	
	
}

