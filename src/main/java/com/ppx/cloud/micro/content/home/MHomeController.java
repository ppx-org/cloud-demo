package com.ppx.cloud.micro.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	// 合成一个
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSwiper() {
		List<String> list = serv.listSwiper();
		
		return ControllerReturn.ok(list);
	}
	
	
	
	
	
}

