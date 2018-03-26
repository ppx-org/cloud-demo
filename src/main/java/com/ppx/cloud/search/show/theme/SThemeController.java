package com.ppx.cloud.search.show.theme;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.search.common.SGrantContext;
import com.ppx.cloud.search.common.Session;


@Controller	
public class SThemeController {
	
	@Autowired
	private SThemedService serv;

	
	@GetMapping @ResponseBody
	public Map<String, Object> listTheme(Session s) {
		SGrantContext.setSession(s);
		
		List<STheme> list = serv.listTheme(s);
		
		return ControllerReturn.ok(list);
	}
	
}

