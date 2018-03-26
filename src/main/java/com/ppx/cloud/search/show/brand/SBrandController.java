package com.ppx.cloud.search.show.brand;

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
public class SBrandController {
	
	@Autowired
	private SBrandService serv;

	
	@GetMapping @ResponseBody
	public Map<String, Object> listBrand(Session s) {
		SGrantContext.setSession(s);
		
		List<SBrand> list = serv.listBrand(s);
		return ControllerReturn.ok(list);
	}
	
}

