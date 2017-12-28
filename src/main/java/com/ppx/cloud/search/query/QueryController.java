package com.ppx.cloud.search.query;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.query.bean.QueryPageList;


@Controller	
public class QueryController {
	
	@Autowired
	private QueryService serv;
	
	

	@RequestMapping @ResponseBody
	public Map<String, Object> q(@RequestParam String w) {
		
		QueryPageList list = serv.query(w);
		
	
		
//		List<Integer> list = serv.listCatId("V1");
//		System.out.println("xxxxxxxxxxxxxxlist:" + list);
		
		
		
		return ControllerReturn.ok(list);
	}
	
}

