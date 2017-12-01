package com.ppx.cloud.merchant.category;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller
public class CategoryController {

	@Autowired
	private CategoryService serv;
	
	@GetMapping
    public ModelAndView listCategory() {		
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listJson() {
		List<Category> list = serv.listCategory();
		return ControllerReturn.ok(list);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertCategory(Category bean) {
		int r = serv.insertCategory(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateCategory(Category bean) {
		int r = serv.updateCategory(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteCategory(Integer catId) {
		int r = serv.deleteCategory(catId);
		return ControllerReturn.ok(r);
	}
	


}