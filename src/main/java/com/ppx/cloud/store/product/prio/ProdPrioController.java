package com.ppx.cloud.store.product.prio;

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
import com.ppx.cloud.store.merchant.store.Store;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class ProdPrioController {
	
	@Autowired
	private ProdPrioService serv;
	
	@GetMapping
	public ModelAndView listProdPrio() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new ProdPrio()));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, ProdPrio bean) {
		PageList<ProdPrio> list = serv.listProdPrio(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getProdPrio(@RequestParam Integer id) {
		ProdPrio bean = serv.getProdPrio(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateProdPrio(ProdPrio bean) {
		int r = serv.updateProdPrio(bean);
		return ControllerReturn.ok(r);
	}
	
	
}

