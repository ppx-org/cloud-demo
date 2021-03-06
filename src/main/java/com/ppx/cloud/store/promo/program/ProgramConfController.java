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
import com.ppx.cloud.store.promo.program.bean.ProgramChange;
import com.ppx.cloud.store.promo.program.bean.ProgramDependence;
import com.ppx.cloud.store.promo.program.bean.ProgramProduct;
import com.ppx.cloud.store.promo.program.bean.ProgramSpecial;


@Controller	
public class ProgramConfController {
	
	@Autowired
	private ProgramConfService serv;
	
	@Autowired
	private CategoryService catServ;
	
	@Autowired
	private BrandService brandServ;
	
	@Autowired
	private ProgramService progServ;
	

	@GetMapping
	public ModelAndView promoCategory(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("prog", progServ.getProgram(progId));
		mv.addObject("listJson", listProgramCat(progId));
		mv.addObject("listCat", catServ.displayAllCat());
		
		
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
		mv.addObject("prog", progServ.getProgram(progId));
		mv.addObject("listJson", listProgramBrand(progId));
		mv.addObject("listBrand", brandServ.listBrand(1));
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
	
	
	
	
	
	// -----------------------------promoProduct-----------------------------
	
	@GetMapping
	public ModelAndView promoProduct(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("prog", progServ.getProgram(progId));
		
		ProgramProduct bean = new ProgramProduct();
		bean.setProgId(progId);
		mv.addObject("listJson", listProgramProduct(new Page(), bean));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramProduct(Page page, ProgramProduct bean) {
		PageList<ProgramProduct> list = serv.listProgramProduct(page, bean);
		return ControllerReturn.ok(list);
	}

	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramProduct(@RequestParam Integer progId, @RequestParam String prodIdStr) {
		String r = serv.insertProgramProduct(progId, prodIdStr);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramProduct(@RequestParam Integer progId, @RequestParam Integer prodId) {
		int r = serv.deleteProgramProduct(progId, prodId);
		return ControllerReturn.ok(r);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ---------------------special----------------------
	@GetMapping
	public ModelAndView promoSpecial(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("prog", progServ.getProgram(progId));
		
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
	public Map<String, Object> insertProgramSpecial(@RequestParam Integer progId,
			@RequestParam String prodIdStr, @RequestParam String specialPriceStr) {
		String r = serv.insertProgramSpecial(progId, prodIdStr, specialPriceStr);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramSpecial(@RequestParam Integer progId, @RequestParam Integer prodId) {
		int r = serv.deleteProgramSpecial(progId, prodId);
		return ControllerReturn.ok(r);
	}
	
	// ---------------------dependence----------------------
	@GetMapping
	public ModelAndView promoDependence(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("prog", progServ.getProgram(progId));
		
		ProgramDependence bean = new ProgramDependence();
		bean.setProgId(progId);
		mv.addObject("listJson", listProgramDependence(new Page(), bean));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramDependence(Page page, ProgramDependence bean) {
		PageList<ProgramDependence> list = serv.listProgramDependence(page, bean);
		return ControllerReturn.ok(list);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramDependence(@RequestParam Integer progId,
			@RequestParam String prodIdStr, @RequestParam String dependProdIdStr,  @RequestParam String dependPriceStr) {
		String r = serv.insertProgramDependence(progId, prodIdStr, dependProdIdStr, dependPriceStr);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramDependence(@RequestParam Integer progId, @RequestParam Integer prodId) {
		int r = serv.deleteProgramDependence(progId, prodId);
		return ControllerReturn.ok(r);
	}
	
	// ---------------------change----------------------
	@GetMapping
	public ModelAndView promoChange(@RequestParam Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("prog", progServ.getProgram(progId));
		
		ProgramChange bean = new ProgramChange();
		bean.setProgId(progId);
		mv.addObject("listJson", listProgramChange(new Page(), bean));
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProgramChange(Page page, ProgramChange bean) {
		PageList<ProgramChange> list = serv.listProgramChange(page, bean);
		return ControllerReturn.ok(list);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProgramChange(@RequestParam Integer progId,
			@RequestParam String prodIdStr, @RequestParam String changePriceStr) {
		String r = serv.insertProgramChange(progId, prodIdStr, changePriceStr);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteProgramChange(@RequestParam Integer progId, @RequestParam Integer prodId) {
		int r = serv.deleteProgramChange(progId, prodId);
		return ControllerReturn.ok(r);
	}
	
	
}

