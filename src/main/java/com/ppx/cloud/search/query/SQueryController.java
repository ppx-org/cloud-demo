package com.ppx.cloud.search.query;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.search.common.SGrantContext;
import com.ppx.cloud.search.common.Session;
import com.ppx.cloud.search.query.bean.QueryBean;
import com.ppx.cloud.search.query.bean.QueryPageList;
import com.ppx.cloud.search.util.BitSetUtils;


@Controller	
public class SQueryController {
	
	@Autowired
	private SQueryService serv;
	
	@RequestMapping @ResponseBody
	public Map<String, Object> test() {
		return ControllerReturn.ok();
	}
	
	@GetMapping @ResponseBody
	public Map<String, Object> query(QueryBean q, Session s) {
		
		// merchantId和storeId不能为空
		if (StringUtils.isEmpty(s.getmId()) || StringUtils.isEmpty(s.getsId())) {
			return ControllerReturn.fail(100, "mId or sId is null");
		}
		SGrantContext.setSession(s);
		if (!StringUtils.isEmpty(s.getOpenid()) && !StringUtils.isEmpty(q.getW())) {
			// 异步插入搜索历史
			CompletableFuture.runAsync(() -> {
				serv.asynInsertSearchHistory(s.getsId(), s.getOpenid(), q.getW());
			});
		}
		
		
		// 初始化数据
		String orderType = q.getO() == null || q.getO() == 0 ? BitSetUtils.ORDER_NORMAL : BitSetUtils.ORDER_NEW;
		String date = (StringUtils.isEmpty(q.getD()) || q.getD() == 0 ? DateUtils.today() : DateUtils.tomorrow());
		QueryPageList bean = serv.query(s.getmId(), s.getsId(), q.getW(), q.getP(), date, q.getcId(), q.getbId(), 
				q.gettId(), q.getgId(), q.getFast(), orderType);
		
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

