package com.ppx.cloud.micro.content.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.micro.user.favorite.MFavoriteService;


@Controller	
public class MProductController {
	
	@Autowired
	private MProductService serv;
	
	@Autowired
	private MFavoriteService favoriteServ;

	
	@PostMapping @ResponseBody
	public Map<String, Object> getProduct(@RequestParam Integer prodId) {
		
		MProduct product = serv.getProduct(prodId);
		
		boolean favor = favoriteServ.isFavorite(prodId);
		
		Map<String, Object> returnMap = ControllerReturn.ok(product);
		returnMap.put("favor", favor);
		
		return returnMap;
	}
	
}

