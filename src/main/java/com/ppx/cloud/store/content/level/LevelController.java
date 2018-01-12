package com.ppx.cloud.store.content.level;

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
public class LevelController {
	
	@Autowired
	private LevelService serv;
	
	@Autowired
	private StoreService storeServ;
	
	@GetMapping
	public ModelAndView listLevel() {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("storeList", storeServ.listStore());
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(@RequestParam Integer storeId) {
		List<Level> list = serv.listLevel(storeId);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertLevel(Level bean) {
		int r = serv.insertLevel(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getLevel(@RequestParam Integer id) {
		Level bean = serv.getLevel(id);
		return ControllerReturn.ok(bean);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteLevel(@RequestParam Integer id) {
		int r = serv.deleteLevel(id);
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
	
	
	
	
	///////////////////prod/////////////////////
	
	@GetMapping
	public ModelAndView listLevelProd(@RequestParam Integer levelId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("levelId", levelId);
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProdJson(@RequestParam Integer levelId) {
		List<LevelProd> list = serv.listLevelProd(levelId);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertLevelProd(LevelProd bean) {
		int r = serv.insertLevelProd(bean);
		return ControllerReturn.ok(r);
	}
	
}

