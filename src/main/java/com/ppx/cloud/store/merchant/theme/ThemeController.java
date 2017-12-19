package com.ppx.cloud.store.merchant.theme;

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


@Controller	
public class ThemeController {
	
	@Autowired
	private ThemeService serv;
	
	
	@GetMapping
	public ModelAndView listTheme() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson());
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {
		List<Theme> list = serv.listTheme();
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertTheme(Theme bean) {
		int r = serv.insertTheme(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getTheme(@RequestParam Integer id) {
		Theme bean = serv.getTheme(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateTheme(Theme bean) {
		int r = serv.updateTheme(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteTheme(@RequestParam Integer id) {
		int r = serv.deleteTheme(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> top(@RequestParam Integer id) {
		int r = serv.top(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> up(@RequestParam Integer id) {
		int r = serv.up(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> down(@RequestParam Integer id) {
		int r = serv.down(id);
		return ControllerReturn.ok(r);
	}
}

