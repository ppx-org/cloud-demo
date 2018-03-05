package com.ppx.cloud.store.product.prio;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


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
	public Map<String, Object> updateProdPrio(Integer prodId, Integer prodPrio) {
		int r = serv.updateProdPrio(prodId, prodPrio);
		return ControllerReturn.ok(r);
	}
	
	
}

