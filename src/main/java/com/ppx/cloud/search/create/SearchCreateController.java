package com.ppx.cloud.search.create;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class SearchCreateController {
	
	@Autowired
	private SearchCreateService serv;
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> init() {
		
		
		System.out.println("s------------s:");
		serv.createTopicIndex();
		
		
		
		return ControllerReturn.ok();
	}
	
	
}

