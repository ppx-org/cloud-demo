package com.ppx.cloud.store.content.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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

}

