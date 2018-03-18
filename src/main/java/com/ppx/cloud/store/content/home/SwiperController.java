package com.ppx.cloud.store.content.home;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class SwiperController {
	
	@Autowired
	private SwiperService serv;
	
	@Autowired
	private StoreService storeServ;
	
	@GetMapping
	public ModelAndView listSwiper() {
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(@RequestParam Integer storeId) {
		List<Swiper> list = serv.listSwiper(storeId);
		return ControllerReturn.ok(list);
	}
	
	//@CacheEvict(value="listHome", allEntries=true)
	@PostMapping @ResponseBody
	public Map<String, Object> insertSwiper(Swiper bean) {
		int r = serv.insertSwiper(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getSwiper(@RequestParam Integer id) {
		Swiper bean = serv.getSwiper(id);
		return ControllerReturn.ok(bean);
	}
    
	
	//@CacheEvict(value="listHome", allEntries=true)
	@PostMapping @ResponseBody
	public Map<String, Object> deleteSwiper(@RequestParam Integer id) {
		int r = serv.deleteSwiper(id);
		return ControllerReturn.ok(r);
	}
	
	//@CacheEvict(value="listHome", allEntries=true)
	@PostMapping @ResponseBody
	public Map<String, Object> top(@RequestParam Integer storeId, @RequestParam Integer id) {
		int r = serv.top(storeId, id);
		return ControllerReturn.ok(r);
	}
	
	//@CacheEvict(value="listHome", allEntries=true)
	@PostMapping @ResponseBody
	public Map<String, Object> up(@RequestParam Integer storeId, @RequestParam Integer id) {
		int r = serv.up(storeId, id);
		return ControllerReturn.ok(r);
	}
	
	//@CacheEvict(value="listHome", allEntries=true)
	@PostMapping @ResponseBody
	public Map<String, Object> down(@RequestParam Integer storeId, @RequestParam Integer id) {
		int r = serv.down(storeId, id);
		return ControllerReturn.ok(r);
	}
}

