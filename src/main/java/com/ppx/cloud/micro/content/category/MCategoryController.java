package com.ppx.cloud.micro.content.category;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MCategoryController {
	
	@Autowired
	private MCategoryService serv;
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> listCategory() {
	
		List<MCategory> list = serv.listCategory();
		
		return ControllerReturn.ok(list);
	}
	
}

