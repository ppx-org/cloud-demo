package com.ppx.cloud.store.content.img;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class ImgController {
	
	@Autowired
	private ImgService serv;
	
	@GetMapping
	public ModelAndView listImg() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("imgList", serv.listImg());
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> updateImgSrc(@RequestParam(required=true)String type, @RequestParam(required=true)String src) {
		int r = serv.updateImgSrc(type, src);
		return ControllerReturn.ok(r);
	}

}

