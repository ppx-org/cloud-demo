package com.ppx.cloud.small.user.order;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class SOrderController {
	

	
	@GetMapping @ResponseBody
	public Map<String, Object> test() {
		System.out.println("xxxxxxxxxxxxxout:------------------0001");
		
		return ControllerReturn.ok();
	}
	
}

