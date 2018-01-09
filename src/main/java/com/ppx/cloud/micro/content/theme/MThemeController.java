package com.ppx.cloud.micro.content.theme;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MThemeController {
	
	@Autowired
	private MThemedService serv;

	
	@PostMapping @ResponseBody
	public Map<String, Object> listTheme() {
		/**
		 每项的数量，图片
		 */
		
		List<MTheme> list = serv.listTheme();
		
		return ControllerReturn.ok(list);
	}
	
}

