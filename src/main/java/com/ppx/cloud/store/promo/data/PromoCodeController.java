package com.ppx.cloud.store.promo.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller	
public class PromoCodeController {
	
	@Autowired
	private PromoCodeService serv;

	@GetMapping
	public ModelAndView listPromoCode() {
		ModelAndView mv = new ModelAndView();
		serv.insert();
		return mv;
	}
	
	
}

