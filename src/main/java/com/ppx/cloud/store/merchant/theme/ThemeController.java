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
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.demo.module.test.TestBean;


@Controller	
public class ThemeController {
	
	@Autowired
	private ThemeService serv;
	
	
	@GetMapping
	public ModelAndView listTheme() {
		ModelAndView mv = new ModelAndView();
		// 默认显示RECORD_STATUS=1
		mv.addObject("listJson", listJson(1));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Integer status) {
		List<Theme> list = serv.listTheme(status);
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
	public Map<String, Object> restoreTheme(@RequestParam Integer id) {
		int r = serv.restoreTheme(id);
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
	
	
	
	
	
	// 
	@GetMapping
	public ModelAndView themeProduct(@RequestParam Integer themeId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("themeId", themeId);
		ThemeProduct bean = new ThemeProduct();
		bean.setThemeId(themeId);
		mv.addObject("listJson", listThemeProduct(new Page(), bean));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listThemeProduct(Page page, ThemeProduct bean) {
		PageList<ThemeProduct> list = serv.listThemeProduct(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertThemeProduct(@RequestParam Integer themeId, @RequestParam String prodIdStr) {
		String r = serv.insertThemeProduct(themeId, prodIdStr);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteThemeProduct(@RequestParam Integer themeId, @RequestParam Integer prodId) {
		int r = serv.deleteThemeProduct(themeId, prodId);
		return ControllerReturn.ok(r);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

