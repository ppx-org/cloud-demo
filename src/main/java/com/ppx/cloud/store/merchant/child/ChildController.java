package com.ppx.cloud.store.merchant.child;

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
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.bean.MerchantAccount;
import com.ppx.cloud.grant.service.GrantService;
import com.ppx.cloud.grant.service.ResourceService;


@Controller
public class ChildController {

	@Autowired
	private ChildService serv;
	
	@GetMapping
    public ModelAndView listChild() {			
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new MerchantAccount()));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, MerchantAccount child) {
		PageList<MerchantAccount> list = serv.listChild(page, child);
		return ControllerReturn.ok(list, page);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertChild(MerchantAccount bean) {
		int r = serv.insertChild(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getChild(@RequestParam Integer id) {
		MerchantAccount bean = serv.getChild(id);
		return ControllerReturn.ok(bean);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateAccount(MerchantAccount bean) {
		int r = serv.updateAccount(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateMerchantPassword(@RequestParam Integer accountId, @RequestParam String loginPassword) {
		int r = serv.updatePassword(accountId, loginPassword);
		return ControllerReturn.ok(r);
	}
	
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteChild(Integer id) {
		int r = serv.deleteMerchant(id);
		return ControllerReturn.ok(r);
	}
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>子帐号权限
	@Autowired
	private GrantService grantService;
	
	@Autowired
	private ResourceService resourceService;
	
	@GetMapping
    public ModelAndView grantToChild() {			
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listChildAccount(new Page(), new MerchantAccount()));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listChildAccount(Page page, MerchantAccount child) {
		PageList<MerchantAccount> list = serv.listChild(page, child);
		return ControllerReturn.ok(list, page);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getAuthorize(@RequestParam Integer accountId) {		
		Map<?, ?> authorizeMap = grantService.getAuthorize(accountId);
		if (authorizeMap == null) authorizeMap = new HashMap<String, Object>();
 		
		Map<?, ?> resMap = resourceService.getResource();
		if (resMap == null) {
			return ControllerReturn.ok(-1);
		}
		return ControllerReturn.ok(authorizeMap, resMap);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> saveAuthorize(@RequestParam Integer accountId, @RequestParam String resIds) {
		long r = grantService.saveAuthorize(accountId, resIds);
		return ControllerReturn.ok(r);
	}
	

}