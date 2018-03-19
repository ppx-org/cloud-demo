package com.ppx.cloud.micro.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MLogController {
	
	
	@Autowired
	private MLogService serv;
	
	@PostMapping @ResponseBody
	public Map<String, Object> addPromoEntry(@RequestBody MLogPromo promo) {
		// 输入type scene 
		
		int r = serv.addPromoEntry(promo);
		return ControllerReturn.ok(r);
		
	}
	

	
}

