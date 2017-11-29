package com.ppx.cloud.grant.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.service.ResourceService;


@Controller
public class ResourceController {
	
	@Autowired
	private ResourceService resourceService;
	
	
	@GetMapping
    public ModelAndView resource() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("res", getResource());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getResource() {
		@SuppressWarnings("rawtypes")
		Map map = resourceService.getResource();
		if (map == null) {
			return ControllerReturn.ok(-1);
		}
		return ControllerReturn.ok(map);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> saveResource(@RequestParam String tree, String removeIds) {
		resourceService.saveResource(tree, removeIds);
		return ControllerReturn.ok();
	}
		
	@PostMapping @ResponseBody
	public Map<String, Object> getUri(@RequestParam Integer resId) {
		@SuppressWarnings("rawtypes")
		Map map = resourceService.getUri(resId);		
		if (map == null) {
			return ControllerReturn.ok(-1);
		}		
		return ControllerReturn.ok(map);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> saveUri(@RequestParam Integer resId, @RequestParam String uri, Integer menuId) {
		@SuppressWarnings("rawtypes")
		Map map = resourceService.saveUri(resId, uri, menuId);
		return ControllerReturn.ok(map);
	}	
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> removeUri(@RequestParam Integer resId, @RequestParam String uri, @RequestParam int uriIndex) {
		resourceService.removeUri(resId, uri, uriIndex);
		return ControllerReturn.ok();
	}


}