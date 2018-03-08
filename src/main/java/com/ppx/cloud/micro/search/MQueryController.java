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
		String queryString = request.getQueryString() + "&mId=" + mId;
		String json = new RestTemplate().getForObject(searchUrl + "SQuery/query?" + queryString, String.class, "");
		ControllerReturn.returnJson(response, json);
	}
	
}

