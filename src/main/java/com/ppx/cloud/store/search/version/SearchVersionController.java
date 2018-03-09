package com.ppx.cloud.store.search.version;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.common.GrantContext;


@Controller	
public class SearchVersionController {
	
	@Autowired
	private SearchVersionService serv;
	
	@Value("${searchUrl}")
	private String searchUrl;
	
	@GetMapping
	public ModelAndView listVersion(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson());
		mv.addObject("lastUpdate", serv.getLastUpdated());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {
		
		List<SearchVersion> list = serv.listVersion();
		return ControllerReturn.ok(list);
	} 
	
	
	@RequestMapping @ResponseBody
	public void createIndex(String versionName, HttpServletResponse response) {
		int mId = GrantContext.getLoginAccount().getMerchantId();
		int accountId = GrantContext.getLoginAccount().getAccountId();
		String queryString = "versionName=" + versionName + "&mId=" + mId + "&accountId=" + accountId;
		String json = new RestTemplate().getForObject(searchUrl + "SSearchCreate/createIndex?" + queryString, String.class, "");
		ControllerReturn.returnJson(response, json);
	}
	
	@RequestMapping @ResponseBody
	public void useIndex(String versionName, HttpServletResponse response) {
		int mId = GrantContext.getLoginAccount().getMerchantId();
		int accountId = GrantContext.getLoginAccount().getAccountId();
		String queryString = "versionName=" + versionName + "&mId=" + mId + "&accountId=" + accountId;
		String json = new RestTemplate().getForObject(searchUrl + "SSearchCreate/useIndex?" + queryString, String.class, "");
		ControllerReturn.returnJson(response, json);
	}
	
	
}

