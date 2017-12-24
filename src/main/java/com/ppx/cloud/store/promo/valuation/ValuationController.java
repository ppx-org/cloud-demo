package com.ppx.cloud.store.promo.valuation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.util.DateUtils;


@Controller	
public class ValuationController {
	
	@Autowired
	private ValuationService serv;
	
	
	@GetMapping
	public ModelAndView test() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("today", DateUtils.today());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSku(@RequestParam String date, @RequestParam String skuIdStr) {
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		
		String[] skuIdArray = skuIdStr.split(",");
		if (skuIdArray.length == 0) {
			return ControllerReturn.ok(-1);
		}
			
		for (String id : skuIdArray) {
			if (StringUtils.isEmpty(id)) {
				return ControllerReturn.ok(-1);
			}
			Integer skuId;
			try {
				skuId = Integer.parseInt(id);
			} catch (Exception e) {
				return ControllerReturn.ok(-1);
			}
			SkuIndex index = new SkuIndex(skuId, 1);
			skuIndexMap.put(skuId, index);
		}
		
		Map<String, List<SkuIndex>> returnMap = serv.count(skuIndexMap);
		if (returnMap.containsKey("-2")) {
			return ControllerReturn.ok(-2, returnMap.get("-2"));
		}
		else {
			return ControllerReturn.ok(1, returnMap.get("1"));
		}
	}
	
	
	
	
	
}

