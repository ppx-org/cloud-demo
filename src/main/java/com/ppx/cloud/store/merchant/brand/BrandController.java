package com.ppx.cloud.store.merchant.brand;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.store.common.dictionary.Dict;
import com.ppx.cloud.store.content.img.ImgService;


@Controller	
public class BrandController {
	
	@Autowired
	private BrandService serv;
	
	@Autowired
	private ImgService imgServ;
	
	@GetMapping
	public ModelAndView listBrand() {
		ModelAndView mv = new ModelAndView();
		// 默认显示RECORD_STATUS=1
		mv.addObject("listJson", listJson(1));
		
		mv.addObject("imgSrc", imgServ.getImgSrc("brand"));
		mv.addObject("listImgX", Dict.listImgX());
		mv.addObject("listImgY", Dict.listImgY());
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Integer status) {
		List<Brand> list = serv.listBrand(status);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertBrand(Brand bean) {
		int r = serv.insertBrand(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getBrand(@RequestParam Integer id) {
		Brand bean = serv.getBrand(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateBrand(Brand bean) {
		int r = serv.updateBrand(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteBrand(@RequestParam Integer id) {
		int r = serv.deleteBrand(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> restoreBrand(@RequestParam Integer id) {
		int r = serv.restoreBrand(id);
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

