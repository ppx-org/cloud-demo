package com.ppx.cloud.store.search.pre;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class PreSearchController {

	
	
	@GetMapping
	public ModelAndView pre() {
		ModelAndView mv = new ModelAndView();
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> search(@RequestParam String word) {
		
		return ControllerReturn.ok();
	}
	
}

