package com.ppx.cloud.micro.user.trace;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryProduct;


@Controller	
public class MTraceController {
	
	
	@Autowired
	private MTraceService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> addProduct(@RequestParam Integer prodId) {
		int r = serv.addProduct(prodId);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> removeProduct(@RequestParam Integer prodId) {
		int r = serv.removeProduct(prodId);
		return ControllerReturn.ok(r);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProduct(@RequestBody MPage page) {
		
		List<QueryProduct> list = serv.listProduct(page);
		return ControllerReturn.ok(list, page);
		
	}
	
	
}

