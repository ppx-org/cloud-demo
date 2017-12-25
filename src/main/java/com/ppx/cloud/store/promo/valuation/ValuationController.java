package com.ppx.cloud.store.promo.valuation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
	
	private final int NO_EXIST_SKU = -2;
	
	@GetMapping
	public ModelAndView test() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("today", DateUtils.today());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSku(@RequestParam @DateTimeFormat(pattern=DateUtils.DATE_PATTERN) Date date, @RequestParam String skuIdStr) {
		
		
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
		
		Map<Integer, List<SkuIndex>> returnMap = serv.count(date, skuIndexMap);
		if (returnMap.containsKey(NO_EXIST_SKU)) {
			return ControllerReturn.ok(NO_EXIST_SKU, returnMap.get(NO_EXIST_SKU));
		}
		else {
			return ControllerReturn.ok(1, returnMap.get(1));
		}
	}
	
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> testSubmit(@RequestParam @DateTimeFormat(pattern=DateUtils.DATE_PATTERN) Date date, 
			@RequestParam String skuIdStr, @RequestParam String numStr) {
		
		// 传参
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		
		String[] skuIdArray = skuIdStr.split(",");
		String[] numArray = numStr.split(",");
		if (skuIdArray.length == 0 || skuIdArray.length != numArray.length) {
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
		
		Map<Integer, List<SkuIndex>> returnMap = serv.count(date, skuIndexMap);
		
		if (returnMap.containsKey(NO_EXIST_SKU)) {
			return ControllerReturn.ok(NO_EXIST_SKU, returnMap.get(NO_EXIST_SKU));
		}
		else {
			// stat
			int totalNum = 0;
			float totalPrice = 0f;
			List<SkuIndex> skuList = returnMap.get(1);
			for (SkuIndex skuIndex : skuList) {
				totalNum += skuIndex.getNum();
				totalPrice += skuIndex.getItemPrice();
			}
			
			Map<String, Object> statMap = new HashMap<String, Object>();
			statMap.put("totalNum", totalNum);
			statMap.put("totalPrice", totalPrice);
			
			
			return ControllerReturn.ok(1, statMap, skuList);
		}
	}
	
	
	
}

