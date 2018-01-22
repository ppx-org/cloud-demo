package com.ppx.cloud.micro.user.favorite;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MFavoriteController {
	
	
	@Autowired
	private MFavoriteService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> addProduct(@RequestParam Integer prodId) {
		
		int r = serv.addProduct(prodId);
		
		return ControllerReturn.ok(r);
	}
	
}

