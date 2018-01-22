package com.ppx.cloud.search.show.brand;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MBrandController {
	
	@Autowired
	private MBrandService serv;

	
	@PostMapping @ResponseBody
	public Map<String, Object> listBrand() {
	
		
		List<MBrand> list = serv.listBrand();
		
		return ControllerReturn.ok(list);
	}
	
}

