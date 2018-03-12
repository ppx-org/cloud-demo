package com.ppx.cloud.store.product.changeprice;

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
public class ChangePriceController {
	
	@Autowired
	private ChangePriceService serv;
	
	
	
	@GetMapping
	public ModelAndView listChangePrice(Integer skuId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("skuId", skuId);
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Integer skuId) {
		
		PageList<ChangePrice> list = serv.listChangePrice(page, skuId);
		String skuMsg = serv.getSkuMsg(skuId);
		
		return ControllerReturn.ok(list, skuMsg);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> addChangePrice(ChangePrice changePrice) {
		double r = serv.addChangePrice(changePrice);
		return ControllerReturn.ok(r);
	}
	
	
}

