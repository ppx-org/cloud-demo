package com.ppx.cloud.micro.common;

import javax.servlet.http.HttpServletRequest;

public class MGrantFilterUtils {
	
	
	public static WxUser getLoginUser(HttpServletRequest request) {
		WxUser u = new WxUser();
		u.setOpenid("deng");
		
		return u;	
	}
}
