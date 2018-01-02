package com.ppx.cloud.store.search.pre;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.util.BitSetUtils;


@Controller	
public class PreSearchController {

	
	
	@GetMapping
	public ModelAndView search() {
		ModelAndView mv = new ModelAndView();
		return mv;
	}

	
	@GetMapping
	public ModelAndView promo(Integer pId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("pId", pId);
		return mv;
	}
	
	@GetMapping
	public ModelAndView cat(Integer cId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cId", cId);
		return mv;
	}
	
	@GetMapping
	public ModelAndView brand(Integer bId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("bId", bId);
		return mv;
	}
	
	@GetMapping
	public ModelAndView theme(Integer tId) {
		System.out.println("xxxxxxxxxxxxx001:" + BitSetUtils.getVersionName(-1));
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("tId", tId);
		return mv;
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> search(@RequestParam String word) {
		return ControllerReturn.ok();
	}
	
}

