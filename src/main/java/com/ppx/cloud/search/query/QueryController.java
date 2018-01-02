package com.ppx.cloud.search.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.search.query.bean.QueryPage;
import com.ppx.cloud.search.query.bean.QueryPageList;


@Controller	
public class QueryController {
	
	@Autowired
	private QueryService serv;
	
	

	@RequestMapping @ResponseBody
	public Map<String, Object> q(@RequestParam String w, QueryPage p, Integer cId, Integer gId, Integer fast) {
		
		
		String date = DateUtils.today();
		QueryPageList bean = serv.query(w, p, date, cId, gId, fast);
		
	
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("catList", bean.getCatList());
		returnMap.put("promoList", bean.getPromoList());
		returnMap.put("fastN", bean.getFastN());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	@RequestMapping @ResponseBody
	public Map<String, Object> cat(QueryPage p, Integer cId) {
		Integer storeId = 1;
		String date = DateUtils.today();
		QueryPageList bean = serv.queryCat(storeId, date, p, cId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("catList", bean.getCatList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	@RequestMapping @ResponseBody
	public Map<String, Object> brand(QueryPage p, Integer bId) {
		Integer storeId = 1;
		String date = DateUtils.today();
		QueryPageList bean = serv.queryBrand(storeId, date, p, bId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("brandList", bean.getBrandList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	@RequestMapping @ResponseBody
	public Map<String, Object> theme(QueryPage p, Integer tId) {
		Integer storeId = 1;
		String date = DateUtils.today();
		QueryPageList bean = serv.queryTheme(storeId, date, p, tId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("themeList", bean.getThemeList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}

	
	@RequestMapping @ResponseBody
	public Map<String, Object> promo(QueryPage p, Integer gId) {
		Integer storeId = 1;
		String date = DateUtils.today();
		QueryPageList bean = serv.queryPromo(storeId, date, p, gId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("promoList", bean.getPromoList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	
	
	
	
	
	
	
	
}

