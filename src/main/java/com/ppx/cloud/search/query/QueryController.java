package com.ppx.cloud.search.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.query.bean.QueryPage;
import com.ppx.cloud.search.query.bean.QueryPageList;


@Controller	
public class QueryController {
	
	@Autowired
	private QueryService serv;
	
	

	@RequestMapping @ResponseBody
	public Map<String, Object> q(@RequestParam String w, QueryPage p, Integer cId) {
		
		QueryPageList bean = serv.query(w, p, cId);
		
	
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("catList", bean.getCatList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
}

