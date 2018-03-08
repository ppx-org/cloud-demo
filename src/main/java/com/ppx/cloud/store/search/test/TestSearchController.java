package com.ppx.cloud.store.search.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class TestSearchController {
	
	@Autowired
	private StoreService storeServ;
	
	@Value("${searchUrl}")
	private String searchUrl;
	
	@GetMapping
	public ModelAndView testSearch() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}
	
	@RequestMapping @ResponseBody
	public void query(HttpServletRequest request, HttpServletResponse response) {
		int mId = GrantContext.getLoginAccount().getMerchantId();
		String queryString = request.getQueryString() + "&mId=" + mId;
		String json = new RestTemplate().getForObject(searchUrl + "SQuery/query?" + queryString, String.class, "");
		ControllerReturn.returnJson(response, json);
	}
	
}

