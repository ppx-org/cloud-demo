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
import com.ppx.cloud.store.merchant.store.Store;
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
		List<Store> storeList = storeServ.listStore();
		mv.addObject("storeList", storeList);
		if (storeList.size() > 0) {
			mv.addObject("listJson", listJson(storeList.get(0).getStoreId()));
		}
		
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
	public Map<String, Object> updateLevel(Level bean) {
		int r = serv.updateLevel(bean);
		return ControllerReturn.ok(r);
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
		mv.addObject("levelName", serv.getLevel(levelId).getLevelName());
		
		mv.addObject("listJson", listLevelProdJson(levelId));
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
	
	@PostMapping @ResponseBody
	public Map<String, Object> deleteLevelProd(@RequestParam Integer levelId, @RequestParam Integer id) {
		int r = serv.deleteLevelProd(levelId, id);
		return ControllerReturn.ok(r);
	}
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> prodTop(@RequestParam Integer levelId, @RequestParam Integer id) {
		int r = serv.prodTop(levelId, id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> prodUp(@RequestParam Integer levelId, @RequestParam Integer id) {
		int r = serv.prodUp(levelId, id);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> prodDown(@RequestParam Integer levelId, @RequestParam Integer id) {
		int r = serv.prodDown(levelId, id);
		return ControllerReturn.ok(r);
	}
	
}

