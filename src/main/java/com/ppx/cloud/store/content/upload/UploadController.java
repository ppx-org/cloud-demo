package com.ppx.cloud.store.content.upload;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.demo.module.test.TestBean;
import com.ppx.cloud.grant.common.GrantContext;


@Controller	
public class UploadController {
	
	@Autowired
	private UploadService serv;
	
	@GetMapping
	public ModelAndView img() {
		ModelAndView mv = new ModelAndView();
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		mv.addObject("merchantId", merchantId);
		mv.addObject("imgList", serv.listImg());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateImgUrl(@RequestParam(required=true)String type, @RequestParam(required=true)String url) {
		int r = serv.updateImgUrl(type, url);
		return ControllerReturn.ok(r);
	}

}

