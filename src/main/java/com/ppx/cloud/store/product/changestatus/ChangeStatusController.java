package com.ppx.cloud.store.product.changestatus;

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
public class ChangeStatusController {
	
	@Autowired
	private ChangeStatusService serv;
	
	
	@GetMapping
	public ModelAndView listChangeStatus(Integer prodId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("prodId", prodId);
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Integer prodId) {
		PageList<ChangeStatus> list = serv.listChangeStatus(page, prodId);
		String prodMsg = serv.getProdMsg(prodId);
		return ControllerReturn.ok(list, prodMsg);
	}
	
	
	
}

