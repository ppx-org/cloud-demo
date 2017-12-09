package com.ppx.cloud.store.promo.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.store.merchant.brand.BrandService;
import com.ppx.cloud.store.merchant.category.CategoryService;
import com.ppx.cloud.store.promo.subject.SubjectService;
import com.ppx.cloud.store.promo.util.PolicyUtils;


@Controller	
public class ProgramConfController {
	
	
	@Autowired
	private CategoryService catServ;
	
	@Autowired
	private BrandService brandServ;
	
	@Autowired
	private SubjectService subjectServ;
	
	
	
	
	@GetMapping
	public ModelAndView category() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listCat", catServ.displayAllCat());
		mv.addObject("listCatPolicy", PolicyUtils.listCatPolicy());
		return mv;
	}
	
	@GetMapping
	public ModelAndView promoBrand() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listBrand", brandServ.listBrand());
		mv.addObject("listBrandPolicy", PolicyUtils.listBrandPolicy());
		return mv;
	}
	
	@GetMapping
	public ModelAndView promoSubject() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listSubject", subjectServ.listSubject());
		mv.addObject("listSubjectPolicy", PolicyUtils.listSubjectPolicy());
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

