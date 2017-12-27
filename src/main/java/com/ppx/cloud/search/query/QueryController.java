package com.ppx.cloud.search.query;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class QueryController {
	
	@Autowired
	private QueryService serv;
	
	

	@RequestMapping @ResponseBody
	public Map<String, Object> q(@RequestParam String w) {
		
		List<Integer> prodIdList = serv.findProdId(w);
		//List<QueryProduct> list = serv.listProduct(prodIdList);
		
		return ControllerReturn.ok(prodIdList);
	}
	
}

