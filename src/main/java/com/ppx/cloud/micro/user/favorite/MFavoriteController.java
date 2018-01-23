package com.ppx.cloud.micro.user.favorite;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.storecommon.query.bean.QueryProduct;


@Controller	
public class MFavoriteController {
	
	
	@Autowired
	private MFavoriteService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> addProduct(@RequestParam Integer prodId) {
		
		int r = serv.addProduct(prodId);
		return ControllerReturn.ok(r);
		
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listProduct() {
		
		List<QueryProduct> list = serv.listProduct();
		return ControllerReturn.ok(list);
		
	}
	
	
}

