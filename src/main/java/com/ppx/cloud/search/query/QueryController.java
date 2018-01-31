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
import com.ppx.cloud.storecommon.page.MPage;


@Controller	
public class QueryController {
	
	@Autowired
	private QueryService serv;
	
	@RequestMapping @ResponseBody
	public Map<String, Object> test() {
		return ControllerReturn.ok();
	}
	
	// pc端接口
	@RequestMapping @ResponseBody
	public Map<String, Object> pcQuery(@RequestParam(required=true) Integer sId, Integer d,
			String w, MPage p, Integer cId, Integer bId, Integer tId,
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
	
	
	
	
	
	
	
	
}

