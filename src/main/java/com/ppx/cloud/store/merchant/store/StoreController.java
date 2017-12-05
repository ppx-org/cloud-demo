package com.ppx.cloud.store.merchant.store;

import java.util.List;
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
import com.ppx.cloud.store.merchant.repo.Repository;
import com.ppx.cloud.store.merchant.repo.RepositoryService;


@Controller	
public class StoreController {
	
	@Autowired
	private StoreService serv;
	
	@Autowired
	private RepositoryService repoServ;
	
	@GetMapping
	public ModelAndView listStore() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new Store()));
		mv.addObject("listRepo", repoServ.listRepository());
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Store bean) {
		PageList<Store> list = serv.listStore(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertStore(Store bean) {
		int r = serv.insertStore(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getStore(@RequestParam Integer id) {
		Store bean = serv.getStore(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateStore(Store bean) {
		int r = serv.updateStore(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteStore(@RequestParam Integer id) {
		int r = serv.deleteStore(id);
		return ControllerReturn.ok(r);
	}
	
	// repo
	@PostMapping @ResponseBody
	public Map<String, Object> listStoreRepo(@RequestParam Integer id) {
		List<Repository> list = repoServ.listStoreRepo(id);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> saveStoreRepo(@RequestParam Integer id, @RequestParam Integer[] repoId) {
		int r = serv.saveStoreRepo(id, repoId);
		return ControllerReturn.ok(r);
	}
	
	
	

}

