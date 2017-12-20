package com.ppx.cloud.store.search.extword;

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


@Controller	
public class ExtWordController {
	
	@Autowired
	private ExtWordService serv;
	
	
	@GetMapping
	public ModelAndView listExtWord() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new ExtWord()));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, ExtWord bean) {
		PageList<ExtWord> list = serv.listTest(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertExtWord(ExtWord bean) {
		int r = serv.insertExtWord(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteExtWord(@RequestParam String id) {
		int r = serv.deleteExtWord(id);
		return ControllerReturn.ok(r);
	}
}

