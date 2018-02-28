package com.ppx.cloud.store.search.pre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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

	
	
}

