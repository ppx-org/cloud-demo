package com.ppx.cloud.store.promo.program;

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
import com.ppx.cloud.store.merchant.brand.BrandService;
import com.ppx.cloud.store.merchant.category.CategoryService;
import com.ppx.cloud.store.promo.program.bean.ProgramBrand;
import com.ppx.cloud.store.promo.program.bean.ProgramCategory;
import com.ppx.cloud.store.promo.subject.SubjectService;
import com.ppx.cloud.store.promo.util.PolicyUtils;


@Controller	
public class ProgramConfController {
	
	@Autowired
	private ProgramConfService serv;
	
	@Autowired
	private CategoryService catServ;
	
	@Autowired
	private BrandService brandServ;
	
	@Autowired
	private SubjectService subjectServ;
	
	

	@GetMapping
	public ModelAndView promoCategory(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listProgramCat(progId));
		
		mv.addObject("progId", progId);
		mv.addObject("listCat", catServ.displayAllCat());
		mv.addObject("listCatPolicy", PolicyUtils.listCatPolicy());
		
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramCat(@RequestParam Integer progId) {
		List<ProgramCategory> list = serv.listProgramCat(progId);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramCategory(ProgramCategory bean) {
		int r = serv.insertProgramCategory(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramCategory(@RequestParam Integer progId, @RequestParam Integer catId) {
		int r = serv.deleteProgramCategory(progId, catId);
		return ControllerReturn.ok(r);
	}
	
	
	// -----------------------------brand--------------------------------
	
	@GetMapping
	public ModelAndView promoBrand() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listBrand", brandServ.listBrand());
		mv.addObject("listBrandPolicy", PolicyUtils.listBrandPolicy());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramBrand(@RequestParam Integer progId) {
		List<ProgramBrand> list = serv.listProgramBrand(progId);
		return ControllerReturn.ok(list);
	}

	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramBrand(ProgramBrand bean) {
		int r = serv.insertProgramBrand(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramBrand(@RequestParam Integer progId, @RequestParam Integer brandId) {
		int r = serv.deleteProgramBrand(progId, brandId);
		return ControllerReturn.ok(r);
	}
	
	// -----------------------------subject-----------------------------
	
	@GetMapping
	public ModelAndView promoSubject() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listSubject", subjectServ.listSubject());
		mv.addObject("listSubjectPolicy", PolicyUtils.listSubjectPolicy());
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

