package com.ppx.cloud.micro.user.cart;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.storecommon.price.bean.SkuIndex;


@Controller	
public class MCartController {
	
	@Autowired
	private MCartService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> addSku(@RequestParam Integer skuId) {
		int r = serv.addSku(skuId);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSku() {
		List<SkuIndex> skuIndexList = serv.listSku();
		return ControllerReturn.ok(skuIndexList);
	}
	
}

