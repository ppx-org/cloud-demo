package com.ppx.cloud.micro.content.store;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;


@Controller	
public class MStoreController {
	
	@Autowired
	private MStoreService serv;
	
	//@Cacheable(value = "listStore")
	@PostMapping @ResponseBody
	public Map<String, Object> listStore( ) {
		List<MStore> list = serv.listStore();
		return ControllerReturn.ok(list);
	}
	
	

}

