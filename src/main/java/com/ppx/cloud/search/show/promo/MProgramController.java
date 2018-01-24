package com.ppx.cloud.search.show.promo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MProgramController {
	
	@Autowired
	private MProgramService serv;

	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgram() {
		
		List<MProgram> list = serv.listProgram();
		
		return ControllerReturn.ok(list);
	}
	
}

