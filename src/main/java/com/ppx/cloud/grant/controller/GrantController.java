package com.ppx.cloud.grant.controller;

import java.util.HashMap;
import java.util.List;
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
import com.ppx.cloud.grant.bean.Merchant;
import com.ppx.cloud.grant.bean.MerchantAccount;
import com.ppx.cloud.grant.service.GrantService;
import com.ppx.cloud.grant.service.ResourceService;


@Controller
public class GrantController {
	
	@Autowired
	private GrantService serv;
	
	@Autowired
	private ResourceService resourceService;
	
	@GetMapping
    public ModelAndView grantToMerchant() {			
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listMerchant(new Page(), new Merchant()));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listMerchant(Page page, Merchant mer) {
		List<Merchant> list = serv.listMerchant(page, mer);
		return ControllerReturn.ok(list, page);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getAuthorize(@RequestParam Integer accountId) {		
		Map<?, ?> authorizeMap = serv.getAuthorize(accountId);
		if (authorizeMap == null) authorizeMap = new HashMap<String, Object>();
 		
		Map<?, ?> resMap = resourceService.getResource();
		if (resMap == null) {
			return ControllerReturn.ok(-1);
		}
		return ControllerReturn.ok(authorizeMap, resMap);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> saveAuthorize(@RequestParam Integer accountId, @RequestParam String resIds) {
		long r = serv.saveAuthorize(accountId, resIds);
		return ControllerReturn.ok(r);
	}

}