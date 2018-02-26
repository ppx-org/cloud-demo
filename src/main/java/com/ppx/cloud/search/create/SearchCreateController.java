package com.ppx.cloud.search.create;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class SearchCreateController {
	
	@Autowired
	private SearchCreateService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> createIndex(@RequestParam String versionName) {
		
		int r = serv.createIndex(versionName);
		
		
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> useIndex(String versionName) {

		int r = serv.useIndex(versionName);
		
		
		return ControllerReturn.ok(r);
	}
	
	
	
//	@GetMapping @ResponseBody
//	public Map<String, Object> test() {
//		Map map = serv.initSearchSpecial();
//		System.out.println("out:" + map);
//		
//		
//		return ControllerReturn.ok(map);
//	}
	
	
}

