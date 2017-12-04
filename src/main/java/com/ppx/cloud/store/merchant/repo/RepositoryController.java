package com.ppx.cloud.store.merchant.repo;

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


@Controller	
public class RepositoryController {
	
	@Autowired
	private RepositoryService serv;
	
	
	@GetMapping
	public ModelAndView listRepository() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new Repository()));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Repository bean) {
		PageList<Repository> list = serv.listRepository(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertRepository(Repository bean) {
		int r = serv.insertRepository(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getRepository(@RequestParam Integer id) {
		Repository bean = serv.getRepository(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateRepository(Repository bean) {
		int r = serv.updateRepository(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteRepository(@RequestParam Integer id) {
		int r = serv.deleteRepository(id);
		return ControllerReturn.ok(r);
	}
}

