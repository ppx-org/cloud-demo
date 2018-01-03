package com.ppx.cloud.store.search.pre;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class PreSearchController {
	
	@Autowired
	private StoreService storeServ;
	
	
	@GetMapping
	public ModelAndView search() {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("storeList", storeServ.listStore());
		
		return mv;
	}

	
	@GetMapping
	public ModelAndView promo(Integer pId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}
	
	@GetMapping
	public ModelAndView cat(Integer cId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}
	
	@GetMapping
	public ModelAndView brand() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}
	
	@GetMapping
	public ModelAndView theme() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}
	
	
}

