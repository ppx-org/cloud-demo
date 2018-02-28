package com.ppx.cloud.store.search.hotword;

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
import com.ppx.cloud.store.merchant.store.StoreService;


@Controller	
public class HotWordController {
	
	@Autowired
	private HotWordService serv;
	
	@Autowired
	private StoreService storeServ;
	
	@GetMapping
	public ModelAndView listHotWord() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(@RequestParam Integer storeId) {
		List<HotWord> list = serv.listHotWord(storeId);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertHotWord(HotWord bean) {
		int r = serv.insertHotWord(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getHotWord(@RequestParam Integer id) {
		HotWord bean = serv.getHotWord(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateHotWord(HotWord bean) {
		int r = serv.updateHotWord(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteHotWord(@RequestParam Integer id) {
		int r = serv.deleteHotWord(id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> top(@RequestParam Integer storeId, @RequestParam Integer id) {
		int r = serv.top(storeId, id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> up(@RequestParam Integer storeId, @RequestParam Integer id) {
		int r = serv.up(storeId, id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> down(@RequestParam Integer storeId, @RequestParam Integer id) {
		int r = serv.down(storeId, id);
		return ControllerReturn.ok(r);
	}
}

