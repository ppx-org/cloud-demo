package com.ppx.cloud.store.content.home;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class CleanHomeController {
	
	@Autowired
	private StoreService storeServ;

	@GetMapping
	public ModelAndView cleanHome() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}

	//@CacheEvict(value="listHome", key="#storeId")
	@PostMapping @ResponseBody
	public Map<String, Object> cleanListHome(@RequestParam Integer storeId) {
		return ControllerReturn.ok(1);
	}
}

