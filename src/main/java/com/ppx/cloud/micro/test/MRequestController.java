package com.ppx.cloud.micro.test;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.WxUser;


@Controller	
public class MRequestController {
	
	@GetMapping
	public ModelAndView testRequest(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	@GetMapping
	public ModelAndView testHome(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> test(@RequestBody Map<String, Object> para) {
		// 或者@RequestBody RequestPara para
		System.out.println("I'm here:" + para);
		
		WxUser u = MGrantContext.getWxUser();	
		System.out.println("----------:" + u.getOpenid());
		System.out.println("----------:" + u.getSessionKey());
		System.out.println("----------:" + u.getStoreId());
		
		return ControllerReturn.ok();
	}
	
	
	
}

