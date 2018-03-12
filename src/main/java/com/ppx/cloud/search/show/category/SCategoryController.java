package com.ppx.cloud.search.show.category;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.common.Session;


@Controller	
public class SCategoryController {
	
	@Autowired
	private SCategoryService serv;
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> listCategory(Session s) {
	
		List<SCategory> list = serv.listCategory(s);
		
		return ControllerReturn.ok(list);
	}
	
}

