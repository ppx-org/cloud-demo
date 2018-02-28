package com.ppx.cloud.micro.content.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MProductController {
	
	@Autowired
	private MProductService serv;

	
	@PostMapping @ResponseBody
	public Map<String, Object> getProduct(@RequestParam Integer prodId) {
		
		MProduct product = serv.getProduct(prodId);
		
		return ControllerReturn.ok(product);
	}
	
}

