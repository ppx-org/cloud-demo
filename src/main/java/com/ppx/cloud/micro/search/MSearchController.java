package com.ppx.cloud.micro.search;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MSearchController {

	@Autowired
	private MSearchService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> listWord() {
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("lastWordList", serv.listLastWord());
		returnMap.put("hotWordList", serv.listHotWord());
		
		return returnMap;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteLastWord() {
		int r = serv.deleteLastWord();
		return ControllerReturn.ok(r);
	}
	
	
}

