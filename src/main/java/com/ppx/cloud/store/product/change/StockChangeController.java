package com.ppx.cloud.store.product.change;

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
public class StockChangeController {
	
	@Autowired
	private StockChangeService serv;
	
	
	
	@GetMapping
	public ModelAndView listStockChange(Integer skuId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("skuId", skuId);
		
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Integer skuId) {
		PageList<StockChange> list = serv.listStockChange(page, skuId);
		String skuMsg = "";
		if (list.getList().size() > 0) {
			skuMsg = serv.getSkuMsg(skuId);
		}
		
		return ControllerReturn.ok(list, skuMsg);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> addStockChange(StockChange stockChange) {
		int r = serv.addStockChange(stockChange);
		return ControllerReturn.ok(r);
	}
	
	
}

