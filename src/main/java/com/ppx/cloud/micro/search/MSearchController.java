package com.ppx.cloud.micro.search;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MSearchController {

	@Autowired
	private MSearchService serv;
	
	@GetMapping @ResponseBody
	public Map<String, Object> listWord() {
		List<String> lastWordList = serv.listLastWord();
		List<String> hotWordList = serv.listHotWord();
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("lastWordList", lastWordList);
		returnMap.put("hotWordList", hotWordList);
		
		return returnMap;
	}
	
}

