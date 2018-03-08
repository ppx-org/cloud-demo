package com.ppx.cloud.search.create;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.common.SGrantContext;
import com.ppx.cloud.search.common.Session;


@Controller	
public class SSearchCreateController {
	
	@Autowired
	private SSearchCreateService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> createIndex(Session s, @RequestParam(required=true) String versionName) {
		if (StringUtils.isEmpty(s.getmId()) || StringUtils.isEmpty(s.getAccountId())) {
			return ControllerReturn.fail(100, "mId or accountId is null");
		}
		SGrantContext.setSession(s);
		
		int r = serv.createIndex(versionName);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> useIndex(Session s, @RequestParam(required=true) String versionName) {
		if (StringUtils.isEmpty(s.getmId()) || StringUtils.isEmpty(s.getAccountId())) {
			return ControllerReturn.fail(100, "mId or accountId is null");
		}
		SGrantContext.setSession(s);
		
		int r = serv.useIndex(versionName);
		return ControllerReturn.ok(r);
	}
	
	
	
}

