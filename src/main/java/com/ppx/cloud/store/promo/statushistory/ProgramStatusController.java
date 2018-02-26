package com.ppx.cloud.store.promo.statushistory;

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


@Controller	
public class ProgramStatusController {
	
	@Autowired
	private ProgramStatusService serv;
	
	
	@GetMapping
	public ModelAndView listProgramStatus(Integer progId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("progId", progId);
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Integer progId) {
		PageList<ProgramStatus> list = serv.listProgramStatus(page, progId);
		String progMsg = serv.getProgMsg(progId);
		return ControllerReturn.ok(list, progMsg);
	}
	
	
	
}

