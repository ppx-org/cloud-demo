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
	
	
	private ConfirmOrderItem countPrice(ConfirmOrderPara para) {
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		
		for (int i = 0; i < para.getSkuId().length; i++) {
			SkuIndex skuIndex = new SkuIndex(para.getSkuId()[i], para.getNum()[i]);
			skuIndexMap.put(para.getSkuId()[i], skuIndex);
		}
		
		Map<Integer, List<SkuIndex>> returnMap = priceServ.countPrice(skuIndexMap);
		// stat
		List<SkuIndex> skuIndexList = returnMap.get(1);
		
		int totalNum = 0;
		float totalPrice = 0f;
		for (SkuIndex skuIndex : skuIndexList) {
			totalNum += skuIndex.getNum();
			totalPrice += skuIndex.getItemPrice();
		}
		
		return new ConfirmOrderItem(skuIndexList, totalNum, totalPrice);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> confirmOrder(@RequestBody ConfirmOrderPara para) {
		if (para.getSkuId() == null || para.getNum() == null) {
			return ControllerReturn.fail(1000, "is null");
		}
		else if (para.getSkuId().length != para.getNum().length) {
			return ControllerReturn.fail(1001, "no equal length");
		}
		
		ConfirmOrderItem comfirmOrderItem = countPrice(para);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("totalNum", comfirmOrderItem.getTotalNum());
		returnMap.put("totalPrice", comfirmOrderItem.getTotalPrice());

		return returnMap;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> submitOrder(@RequestBody ConfirmOrderPara para) {
		// TODO 防多次提交
		
		if (para.getSkuId() == null || para.getNum() == null) {
			return ControllerReturn.fail(1000, "is null");
		}
		else if (para.getSkuId().length != para.getNum().length) {
			return ControllerReturn.fail(1001, "no equal length");
		}
		
		ConfirmOrderItem comfirmOrderItem = countPrice(para);
		
		
		int r = serv.submitOrder(comfirmOrderItem, para);
		
		
		return ControllerReturn.ok(r);
	}
	
	

	@PostMapping @ResponseBody
	public Map<String, Object> listMyOrder(@RequestBody MPage page) {

		MPageList<UserOrder> list = serv.listMyOrder(page);
		return ControllerReturn.ok(list);

	}
}
