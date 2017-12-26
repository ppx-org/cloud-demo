package com.ppx.cloud.search.query;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class QueryController {
	
	@Autowired
	private QueryService serv;
	
	

	@PostMapping @ResponseBody
	public Map<String, Object> q(@RequestParam String w) {
		int[] prodId = serv.findProdId(w);
		List<QueryProduct> list = serv.listProduct(prodId);
		
		return ControllerReturn.ok(list);
	}
	
}

