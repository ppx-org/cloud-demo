package com.ppx.cloud.search.show.promo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.common.SGrantContext;
import com.ppx.cloud.search.common.Session;


@Controller	
public class SProgramController {
	
	@Autowired
	private SProgramService serv;

	
	@GetMapping @ResponseBody
	public Map<String, Object> listProgram(Session s) {
		SGrantContext.setSession(s);
		
		List<SProgram> list = serv.listProgram(s);
		
		return ControllerReturn.ok(list);
	}
	
}

