package com.ppx.cloud.micro.user.cart;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.storecommon.price.bean.SkuIndex;
import com.ppx.cloud.storecommon.price.service.PriceCommonService;


@Controller	
public class MCartController {
	
	@Autowired
	private MCartService serv;
	
	@Autowired
	private PriceCommonService priceServ;
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> addSku(@RequestParam Integer skuId) {
		
		int r = serv.addSku(skuId);
		
		return ControllerReturn.ok(r);
	}
	
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSku() {
		
		List<SkuIndex> skuIndexList = serv.listSku();
		
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		for (SkuIndex skuIndex : skuIndexList) {
			skuIndexMap.put(skuIndex.getSkuId(), skuIndex);
		}
		
		Map<Integer, List<SkuIndex>> returnMap = priceServ.countPrice(new Date(), skuIndexMap);
		
		
		
		return ControllerReturn.ok(returnMap.get(1));
	}
	
	
	
}

