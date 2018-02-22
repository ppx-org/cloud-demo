package com.ppx.cloud.store.order.torepo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.store.common.dictionary.Dict;
import com.ppx.cloud.store.merchant.repo.RepositoryService;
import com.ppx.cloud.store.merchant.store.StoreService;
import com.ppx.cloud.storecommon.order.bean.OrderItem;


@Controller	
public class RepoOrderController {
	
	@Autowired
	private RepositoryService repoServ;
	
	@Autowired
	private StoreService storeServ;
	
	@Autowired
	private RepoOrderService serv;
	
	
	@GetMapping
	public ModelAndView listOrderItem() {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("repoList", repoServ.displayRepository());
		mv.addObject("storeList", storeServ.listStore());
		mv.addObject("itemStatusList", Dict.listOrderItemStatus());
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, @RequestParam Integer repoId, OrderItem bean) {
		PageList<OrderItem> list = serv.listOrderItemByRepo(page, repoId, bean);
		return ControllerReturn.ok(list);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> lockItem(@RequestParam Integer itemId) {
		int r = serv.lockItem(itemId);
		String statusDesc = (r == 1) ? Dict.getOrderItemStatusDesc(2) : "";
		return ControllerReturn.ok(r, statusDesc);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> configItem(@RequestParam Integer itemId) {
		int r = serv.configItem(itemId);
		String statusDesc = (r == 1) ? Dict.getOrderItemStatusDesc(3) : "";
		return ControllerReturn.ok(r, statusDesc);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> deliverItem(@RequestParam Integer itemId) {
		int r = serv.deliverItem(itemId);
		String statusDesc = (r == 1) ? Dict.getOrderItemStatusDesc(4) : "";
		return ControllerReturn.ok(r, statusDesc);
	}
	
	
}

