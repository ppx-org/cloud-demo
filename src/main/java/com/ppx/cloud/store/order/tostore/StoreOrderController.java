package com.ppx.cloud.store.order.tostore;

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
import com.ppx.cloud.store.merchant.store.StoreService;
import com.ppx.cloud.storecommon.order.bean.UserOrder;


@Controller	
public class StoreOrderController {
	
	@Autowired
	private StoreService storeServ;
	
	@Autowired
	private StoreOrderService serv;
	
	
	@GetMapping
	public ModelAndView listOrder() {
		ModelAndView mv = new ModelAndView();
		storeServ.listStore();
		
		mv.addObject("storeList", storeServ.listStore());
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, UserOrder bean) {
		PageList<UserOrder> list = serv.listOrder(page, bean);
		return ControllerReturn.ok(list);
	}
	
	
}

