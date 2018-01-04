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
import com.ppx.cloud.store.merchant.repo.RepositoryService;
import com.ppx.cloud.store.merchant.store.StoreService;
import com.ppx.cloud.store.order.bean.OrderItem;
import com.ppx.cloud.store.order.bean.UserOrder;


@Controller	
public class RepoOrderController {
	
	@Autowired
	private RepositoryService repoServ;
	
	@Autowired
	private RepoOrderService serv;
	
	
	@GetMapping
	public ModelAndView listOrderItem() {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("repoList", repoServ.listRepository());
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, @RequestParam Integer repoId, OrderItem bean) {
		PageList<OrderItem> list = serv.listOrderItemByRepo(page, repoId, bean);
		return ControllerReturn.ok(list);
	}
	
	
}

