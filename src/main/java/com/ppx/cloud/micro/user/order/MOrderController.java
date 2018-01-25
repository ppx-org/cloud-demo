package com.ppx.cloud.micro.user.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.storecommon.order.bean.UserOrder;
import com.ppx.cloud.storecommon.page.MPage;
import com.ppx.cloud.storecommon.page.MPageList;
import com.ppx.cloud.storecommon.price.bean.SkuIndex;
import com.ppx.cloud.storecommon.price.service.PriceCommonService;

@Controller

public class MOrderController {

	@Autowired
	private MOrderService serv;

	@Autowired
	private PriceCommonService priceServ;
	
	@PostMapping @ResponseBody
	public Map<String, Object> confirmOrder(@RequestBody ConfirmOrderPara para) {
		if (para.getSkuId() == null || para.getNum() == null) {
			return ControllerReturn.fail(1000, "is null");
		}
		else if (para.getSkuId().length != para.getNum().length) {
			return ControllerReturn.fail(1001, "no equal length");
		}
		
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		
		for (int i = 0; i < para.getSkuId().length; i++) {
			SkuIndex skuIndex = new SkuIndex(para.getSkuId()[i], para.getNum()[i]);
			skuIndexMap.put(para.getSkuId()[i], skuIndex);
		}
		
		Map<Integer, List<SkuIndex>> returnMap = priceServ.countPrice(skuIndexMap);
		
		// stat
		int totalNum = 0;
		float totalPrice = 0f;
		List<SkuIndex> skuList = returnMap.get(1);
		for (SkuIndex skuIndex : skuList) {
			totalNum += skuIndex.getNum();
			totalPrice += skuIndex.getItemPrice();
		}

		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("totalNum", totalNum);
		statMap.put("totalPrice", totalPrice);

		return ControllerReturn.ok(statMap, skuList);
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listMyOrder(@RequestBody MPage page) {

		MPageList<UserOrder> list = serv.listMyOrder(page);
		return ControllerReturn.ok(list);

	}
}
