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
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.common.page.MPageList;
import com.ppx.cloud.demo.common.order.OrderStatusHistory;
import com.ppx.cloud.demo.common.order.UserOrder;
import com.ppx.cloud.demo.common.price.bean.SkuIndex;
import com.ppx.cloud.demo.common.price.service.PriceCommonService;
import com.ppx.cloud.micro.content.store.MStoreService;
import com.ppx.cloud.micro.user.order.bean.ConfirmOrderItem;
import com.ppx.cloud.micro.user.order.bean.ConfirmOrderPara;
import com.ppx.cloud.micro.user.order.bean.ConfirmReturn;

@Controller

public class MOrderController {

	@Autowired
	private MOrderService serv;
	
	@Autowired
	private MStoreService storeServ;

	@Autowired
	private PriceCommonService priceServ;
	
	// 加上store的信息
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
		returnMap.put("skuList", comfirmOrderItem.getSkuIndexList());
		returnMap.put("totalNum", comfirmOrderItem.getTotalNum());
		returnMap.put("totalPrice", comfirmOrderItem.getTotalPrice());
		returnMap.put("store", storeServ.getStore());

		return returnMap;
	}
	
	private ConfirmOrderItem countPrice(@RequestBody ConfirmOrderPara para) {
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
	public Map<String, Object> submitOrder(@RequestBody ConfirmOrderPara para) {
		// TODO 防多次提交
		
		if (para.getSkuId() == null || para.getNum() == null) {
			return ControllerReturn.fail(1000, "is null");
		}
		else if (para.getSkuId().length != para.getNum().length) {
			return ControllerReturn.fail(1001, "no equal length");
		}
		
		ConfirmOrderItem comfirmOrderItem = countPrice(para);
		
		
		ConfirmReturn confirmReturn = serv.submitOrder(comfirmOrderItem, para);
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("result", confirmReturn.getResult());
		returnMap.put("overflowList", confirmReturn.getOverflowList());
		
		
		return returnMap;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listMyOrder(@RequestBody MPage page, Integer orderStatus) {
		MPageList<UserOrder> list = serv.listMyOrder(page, orderStatus);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getDetail(Integer orderId) {
		
		UserOrder order = serv.getOrder(orderId);
		
		List<OrderStatusHistory> statusList = serv.listOrderStatus(orderId);
		
		Map<String, Object> map = ControllerReturn.ok();
		
		map.put("store", storeServ.getStore());
		map.put("order", order);
		map.put("statusList", statusList);
		
		return map;
	}
	
}
