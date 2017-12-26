package com.ppx.cloud.search.version;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class SearchVersionController {
	
	@Autowired
	private SearchVersionService serv;
	
	@GetMapping
	public ModelAndView listVersion(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		
//		serv.listVersion()
//		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("version", "V1");
//		map.put("time", "2017-01-09 12:44:22");
//		list.add(map);
		mv.addObject("list", serv.listVersion());
		
		return mv;
	}
	
	
	
	
}

