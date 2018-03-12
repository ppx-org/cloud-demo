package com.ppx.cloud.micro.search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.micro.common.MGrantContext;


@Controller	
public class MQueryController {
	
	@Value("${searchUrl}")
	private String searchUrl;
	
	@RequestMapping @ResponseBody
	public void query(HttpServletRequest request, HttpServletResponse response) {
		int mId = MGrantContext.getWxUser().getMerchantId();
		int sId = MGrantContext.getWxUser().getStoreId();
		String openid = MGrantContext.getWxUser().getOpenid();
		String queryString = "mId=" + mId + "&sId=" + sId + "&openid=" + openid;
		String json = new RestTemplate().getForObject(searchUrl + "SQuery/query?" + queryString, String.class);
		ControllerReturn.returnJson(response, json);
	}
	
	@RequestMapping @ResponseBody
	public void listCategory(HttpServletRequest request, HttpServletResponse response) {
		int mId = MGrantContext.getWxUser().getMerchantId();
		int sId = MGrantContext.getWxUser().getStoreId();
		String openid = MGrantContext.getWxUser().getOpenid();
		
		String queryString = "mId=" + mId + "&sId=" + sId + "&openid=" + openid;
		String json = new RestTemplate().getForObject(searchUrl + "SCategory/listCategory?" + queryString, String.class);
		ControllerReturn.returnJson(response, json);
	}
	
	@RequestMapping @ResponseBody
	public void listBrand(HttpServletRequest request, HttpServletResponse response) {
		int mId = MGrantContext.getWxUser().getMerchantId();
		int sId = MGrantContext.getWxUser().getStoreId();
		String openid = MGrantContext.getWxUser().getOpenid();
		
		String queryString = "mId=" + mId + "&sId=" + sId + "&openid=" + openid;
		String json = new RestTemplate().getForObject(searchUrl + "SBrand/listBrand?" + queryString, String.class);
		ControllerReturn.returnJson(response, json);
	}
	
	@RequestMapping @ResponseBody
	public void listProgram(HttpServletRequest request, HttpServletResponse response) {
		int mId = MGrantContext.getWxUser().getMerchantId();
		int sId = MGrantContext.getWxUser().getStoreId();
		String openid = MGrantContext.getWxUser().getOpenid();
		
		String queryString = "mId=" + mId + "&sId=" + sId + "&openid=" + openid;
		String json = new RestTemplate().getForObject(searchUrl + "SProgram/listProgram?" + queryString, String.class);
		ControllerReturn.returnJson(response, json);
	}
	
	@RequestMapping @ResponseBody
	public void listTheme(HttpServletRequest request, HttpServletResponse response) {
		int mId = MGrantContext.getWxUser().getMerchantId();
		int sId = MGrantContext.getWxUser().getStoreId();
		String openid = MGrantContext.getWxUser().getOpenid();
		
		String queryString = "mId=" + mId + "&sId=" + sId + "&openid=" + openid;
		String json = new RestTemplate().getForObject(searchUrl + "STheme/listTheme?" + queryString, String.class);
		ControllerReturn.returnJson(response, json);
	}
}

