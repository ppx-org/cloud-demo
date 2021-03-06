package com.ppx.cloud.store.common.init;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.controller.ControllerContext;
import com.ppx.cloud.monitor.AccessLog;
import com.ppx.cloud.monitor.AccessUtils;
import com.ppx.cloud.search.create.SSearchCreateService;


/**
 * 启动初始化
 * @author dengxz
 * @date 2018年2月28日
 */
@Service
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
	
	
	@Autowired
	private SSearchCreateService searchCreateService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event)  {
		AccessLog log = new AccessLog();
		log.setUri("init");
		log.setBeginTime(new Date());
		log.setIp("127.0.0.0");
		log.setMethod("INIT");
		ControllerContext.setAccessLog(log);
		
		searchCreateService.initVersion();
		
		AccessLog accessLog = ControllerContext.getAccessLog();
		accessLog.setSpendTime(System.currentTimeMillis() - accessLog.getBeginTime().getTime());
		AccessUtils.writeQueue(accessLog);
	
	}
}

