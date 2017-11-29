package com.ppx.cloud.grant.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.common.LoginAccount;
import com.ppx.cloud.grant.service.MenuService;
import com.ppx.cloud.grant.service.PasswordService;


@Controller
public class IndexController {
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>首页>>>>>>>>>>>>>>>>>>>
	@GetMapping
    public ModelAndView home() {			
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>修改密码>>>>>>>>>>>>>>>>>>>
	@Autowired
	private PasswordService passwordService;
	
	@GetMapping
    public ModelAndView editPassword() {	
		ModelAndView mv = new ModelAndView();
		return mv;
	}	
	
	@PostMapping @ResponseBody
	public Map<String, Object> updatePassword(@RequestParam String oldP, @RequestParam String newP) {
		int r = passwordService.updatePassword(oldP, newP);
		return ControllerReturn.ok(r);
	}
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>菜单>>>>>>>>>>>>>>>>>>>>>>
	@Autowired
	private MenuService menuService;
	
	@GetMapping
	public ModelAndView menuIndex(HttpServletRequest request) {		
		ModelAndView mv = new ModelAndView();
	
		LoginAccount account = GrantContext.getLoginAccount();
		mv.addObject("account", account);
		if ("admin".equals(account.getLoginAccount())) {			
			mv.addObject("menu", getAdminResource());
		}
		else {
			mv.addObject("menu", menuService.getMenu());
		}
		
		return mv;
	}
	
	private List<Map<String, Object>> getAdminResource() {
		// 超级管理员的固定菜单
		List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();			
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		
		// 菜单项1
		Map<String, Object> menuMap = new LinkedHashMap<String, Object>();
		menuMap.put("t", "资源管理");
		menuMap.put("i", 1);
		menuMap.put("uri", "/resource/resource");
		menuList.add(menuMap);
		
		menuMap = new LinkedHashMap<String, Object>();
		menuMap.put("t", "商户管理");
		menuMap.put("i", 1);
		menuMap.put("uri", "/merchant/listMerchant");
		menuList.add(menuMap);
		
		menuMap = new LinkedHashMap<String, Object>();
		menuMap.put("t", "权限管理");
		menuMap.put("i", 1);
		menuMap.put("uri", "/grant/grantToMerchant");
		menuList.add(menuMap);
		
		
		// 目录项0
		Map<String, Object> systemMap = new LinkedHashMap<String, Object>();
		systemMap.put("t", "系统管理");
		systemMap.put("i", 0);
		systemMap.put("n", menuList);
		
		dirList.add(systemMap);
		return dirList;
	}
	
}