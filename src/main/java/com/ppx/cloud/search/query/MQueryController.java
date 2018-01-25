package com.ppx.cloud.search.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.query.bean.MQueryBean;
import com.ppx.cloud.search.query.bean.QueryPageList;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.storecommon.page.MQueryPage;


@Controller	
public class MQueryController {
	
	@Autowired
	private QueryService serv;
	
	// 移动端接口
	@PostMapping @ResponseBody
	public Map<String, Object> q(@RequestBody MQueryBean b) {
		Integer sId = MGrantContext.getWxUser().getStoreId();
		
		// 分类少的时候可以放开
		if (StringUtils.isEmpty(b.getW()) && b.getCid() == null && b.getBid() == null
			&& b.getTid() == null && b.getGid() == null) {
			return null;
		}
		
		return testQuery(sId, 0, b.getW(), b.getP(), 
				b.getCid(), b.getBid(), b.getTid(), b.getGid(), b.getFast(), b.getO());
	}
	
	
	// pc端接口
	@RequestMapping @ResponseBody
	public Map<String, Object> testQuery(@RequestParam(required=true) Integer sId, Integer d,
			String w, MQueryPage p, Integer cId, Integer bId, Integer tId,
			Integer gId, Integer fast, Integer o) {
		
		
		String orderType = StringUtils.isEmpty(o) || o == 0 ? BitSetUtils.ORDER_NORMAL + BitSetUtils.getCurrentVersionName() 
			: BitSetUtils.ORDER_NEW + BitSetUtils.getCurrentVersionName();

		String date = (StringUtils.isEmpty(o) || d == 0 ? DateUtils.today() : DateUtils.tomorrow());
		QueryPageList bean = serv.query(sId, w, p, date, cId, bId, tId, gId, fast, orderType);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fastN", bean.getFastN());
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("catList", bean.getCatList());
		returnMap.put("brandList", bean.getBrandList());
		returnMap.put("themeList", bean.getThemeList());
		returnMap.put("promoList", bean.getPromoList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	// 使用在搜索词转名称再转换成ID
	/*
	@RequestMapping @ResponseBody
	public Map<String, Object> cat(@RequestParam Integer sId, QueryPage p, Integer cId) {
		QueryPageList bean = serv.queryCat(sId, p, cId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("catList", bean.getCatList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	@RequestMapping @ResponseBody
	public Map<String, Object> brand(@RequestParam Integer sId, QueryPage p, Integer bId) {
		QueryPageList bean = serv.queryBrand(sId, p, bId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("brandList", bean.getBrandList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}
	
	@RequestMapping @ResponseBody
	public Map<String, Object> theme(@RequestParam Integer sId, QueryPage p, Integer tId) {
		QueryPageList bean = serv.queryTheme(sId, p, tId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("themeList", bean.getThemeList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}

	
	@RequestMapping @ResponseBody
	public Map<String, Object> promo(@RequestParam Integer sId, @RequestParam Integer d, QueryPage p, Integer gId) {
		String date = (d == 1 ? DateUtils.today() : DateUtils.tomorrow());
		QueryPageList bean = serv.queryPromo(sId, date, p, gId);
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("arrayList", bean.getProdList());
		returnMap.put("promoList", bean.getPromoList());
		returnMap.put("page", bean.getQueryPage());
		
		return ControllerReturn.ok(returnMap);
	}*/
	
	
	
	
	
	
	
	
	
}

