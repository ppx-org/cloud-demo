package com.ppx.cloud.store.promo.program;

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
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.store.merchant.category.CategoryService;
import com.ppx.cloud.store.promo.util.PolicyUtils;


@Controller	
public class ProgramController {
	
	@Autowired
	private ProgramService serv;
	
	
	@GetMapping
	public ModelAndView listProgram() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new Program()));
		mv.addObject("listPolicy", PolicyUtils.listPolicyType());
		mv.addObject("listProductPolicy", PolicyUtils.listProductPolicy());
		mv.addObject("listCatPolicy", PolicyUtils.listCatPolicy());
		mv.addObject("listBrandPolicy", PolicyUtils.listBrandPolicy());
		
		mv.addObject("today", DateUtils.today());
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Program bean) {
		PageList<Program> list = serv.listProgram(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProgram(Program bean) {
		int r = serv.insertProgram(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getProgram(@RequestParam Integer id) {
		Program bean = serv.getProgram(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateProgram(Program bean) {
		int r = serv.updateProgram(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgram(@RequestParam Integer id) {
		int r = serv.deleteProgram(id);
		return ControllerReturn.ok(r);
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
}

