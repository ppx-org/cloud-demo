package com.ppx.cloud.store.product.adjust;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Controller	
public class PriceAdjustController {
	
	@Autowired
	private PriceAdjustService serv;
	
	
	
	@GetMapping
	public ModelAndView listPriceAdjust(Integer skuId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("skuId", skuId);
		
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Integer skuId) {
		
		PageList<PriceAdjust> list = serv.listPriceAdjust(page, skuId);
		String skuMsg = serv.getSkuMsg(skuId);
		
		
		return ControllerReturn.ok(list, skuMsg);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> addPriceAdjust(PriceAdjust priceAdjust) {
		double r = serv.addPriceAdjust(priceAdjust);
		return ControllerReturn.ok(r);
	}
	
	
}

