package com.ppx.cloud.grant.controller;

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
import com.ppx.cloud.grant.bean.Merchant;
import com.ppx.cloud.grant.bean.MerchantAccount;
import com.ppx.cloud.grant.service.MerchantService;


@Controller
public class MerchantController {

	@Autowired
	private MerchantService serv;
	
	@GetMapping
    public ModelAndView listMerchant() {			
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new Merchant()));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Merchant mer) {
		PageList<Merchant> list = serv.listMerchant(page, mer);
		return ControllerReturn.ok(list, page);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertMerchant(Merchant bean) {
		int r = serv.insertMerchant(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getMerchant(@RequestParam Integer id) {
		Merchant bean = serv.getMerchant(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getMerchantAccount(@RequestParam Integer id) {
		MerchantAccount bean = serv.getMerchantAccount(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateMerchant(Merchant bean) {
		int r = serv.updateMerchant(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateMerchantAccount(MerchantAccount bean) {
		int r = serv.updateMerchantAccount(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateMerchantPassword(@RequestParam Integer merchantId, @RequestParam String merchantPassword) {
		int r = serv.updateMerchantPassword(merchantId, merchantPassword);
		return ControllerReturn.ok(r);
	}
	
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteMerchant(Integer id) {
		int r = serv.deleteMerchant(id);
		return ControllerReturn.ok(r);
	}

}