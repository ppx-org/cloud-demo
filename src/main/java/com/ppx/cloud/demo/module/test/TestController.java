package com.ppx.cloud.demo.module.test;

import java.util.BitSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

/**
 * 访问uri:${contextPath}/test/listTest
 * \@GetMapping对应ModelAndView, \@PostMapping对应\@ResponseBody
 * @author dengxz
 * @date 2017年11月14日
 */
@Controller	
public class TestController {
	
	@Autowired
	private TestService serv;
	
	@GetMapping
	public ModelAndView myTest(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		
		
		
		
		
		//serv.test();
		
	
	
		
		
		
		return mv;
	}
	
	@GetMapping @ResponseBody
	public Map<String, Object> test() {
	
		
		
		
		
		
		
		
		
		
		
		
		//serv.test();
		return ControllerReturn.ok();
	}
	
	@GetMapping
	public ModelAndView listTest() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new TestBean()));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, TestBean bean) {
		PageList<TestBean> list = serv.listTest(page, bean);
		return ControllerReturn.ok(list);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertTest(TestBean bean) {
		int r = serv.insertTest(bean);
		return ControllerReturn.ok(r);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> getTest(@RequestParam Integer id) {
		TestBean bean = serv.getTest(id);
		return ControllerReturn.ok(bean);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateTest(TestBean bean) {
		int r = serv.updateTest(bean);
		return ControllerReturn.ok(r);
	}
    
	@PostMapping @ResponseBody
	public Map<String, Object> deleteTest(@RequestParam Integer id) {
		int r = serv.deleteTest(id);
		return ControllerReturn.ok(r);
	}
}

