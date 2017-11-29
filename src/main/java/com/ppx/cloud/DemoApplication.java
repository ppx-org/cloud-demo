package com.ppx.cloud;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 启动类
 * @author dengxz
 * @date 2017年11月14日
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	// 为了让firstConfigBean先运行 (@ComponentScan自动扫描之后@Order不生效)
	@Resource(name = "firstConfigRun")
	private Object firstConfigBean;
}

