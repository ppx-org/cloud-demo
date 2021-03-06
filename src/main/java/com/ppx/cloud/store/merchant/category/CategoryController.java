package com.ppx.cloud.store.merchant.category;

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
import com.ppx.cloud.store.common.dictionary.Dict;
import com.ppx.cloud.store.content.img.ImgService;


@Controller
public class CategoryController {

	@Autowired
	private CategoryService serv;
	
	@Autowired
	private ImgService imgServ;
	
	@GetMapping
    public ModelAndView listCategory() {		
		ModelAndView mv = new ModelAndView();
		
		// 默认为有效的RECORD_STATUS=1
		mv.addObject("listJson", listJson(1));
		
		
		
		mv.addObject("imgSrc", imgServ.getImgSrc("cat"));
		mv.addObject("listImgX", Dict.listImgX());
		mv.addObject("listImgY", Dict.listImgY());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Integer status) {
		List<Category> list = serv.listCategory(status);
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
	public Map<String, Object> deleteCategory(@RequestParam Integer id) {
		int r = serv.deleteCategory(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> restoreCategory(@RequestParam Integer id) {
		int r = serv.restoreCategory(id);
		return ControllerReturn.ok(r);
	}
	

	@PostMapping @ResponseBody
	public Map<String, Object> top(@RequestParam Integer id) {
		int r = serv.top(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> last(@RequestParam Integer id) {
		int r = serv.last(id);
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