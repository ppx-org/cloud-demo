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
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.store.merchant.brand.BrandService;
import com.ppx.cloud.store.merchant.category.CategoryService;
import com.ppx.cloud.store.promo.program.bean.ProgramBrand;
import com.ppx.cloud.store.promo.program.bean.ProgramCategory;
import com.ppx.cloud.store.promo.program.bean.ProgramSpecial;
import com.ppx.cloud.store.promo.program.bean.ProgramSubject;
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
		mv.addObject("progId", progId);
		mv.addObject("listJson", listProgramCat(progId));
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
	public ModelAndView promoBrand(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("progId", progId);
		mv.addObject("listJson", listProgramBrand(progId));
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
	public ModelAndView promoSubject(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("progId", progId);
		mv.addObject("listJson", listProgramSubject(progId));
		mv.addObject("listSubject", subjectServ.listSubject());
		mv.addObject("listSubjectPolicy", PolicyUtils.listSubjectPolicy());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramSubject(@RequestParam Integer progId) {
		List<ProgramSubject> list = serv.listProgramSubject(progId);
		return ControllerReturn.ok(list);
	}

	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramSubject(ProgramSubject bean) {
		int r = serv.insertProgramSubject(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramSubject(@RequestParam Integer progId, @RequestParam Integer subjectId) {
		int r = serv.deleteProgramSubject(progId, subjectId);
		return ControllerReturn.ok(r);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ---------------------special----------------------
	@GetMapping
	public ModelAndView promoSpecial(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("progId", progId);
		ProgramSpecial bean = new ProgramSpecial();
		bean.setProgId(progId);
		mv.addObject("listJson", listProgramSpecial(new Page(), bean));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramSpecial(Page page, ProgramSpecial bean) {
		PageList<ProgramSpecial> list = serv.listProgramSpecial(page, bean);
		return ControllerReturn.ok(list);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramSpecial(@RequestParam Integer progId, @RequestParam Integer[] prodId) {
		int r = serv.insertProgramSpecial(progId, prodId);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramSpecial(@RequestParam Integer progId, @RequestParam Integer prodId) {
		int r = serv.deleteProgramSpecial(progId, prodId);
		return ControllerReturn.ok(r);
	}
	
	
	
	
	
	
}

