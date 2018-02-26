package com.ppx.cloud.search.version;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

		mv.addObject("listJson", listJson());
		
		mv.addObject("lastUpdate", serv.getLastUpdated());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {

		List<SearchVersion> list = serv.listVersion();
		
		return ControllerReturn.ok(list);
	} 
	
	
	
	
}

