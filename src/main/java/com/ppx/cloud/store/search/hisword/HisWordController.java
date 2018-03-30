package com.ppx.cloud.store.search.hisword;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class HisWordController {
	
	@Autowired
	private HisWordService serv;
	
	@Autowired
	private StoreService storeServ;
	
	@GetMapping
	public ModelAndView listHisWord() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(HisWord hitWord, Page page) {
		List<HisWord> list = serv.listHisWord(hitWord, page);
		return ControllerReturn.ok(list, page);
	}
	
	
}

