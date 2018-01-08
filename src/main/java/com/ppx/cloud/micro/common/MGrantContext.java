package com.ppx.cloud.micro.common;

/**
 * 分配权限上下文
 * @author dengxz
 * @date 2017年11月10日
 */
public class MGrantContext {
	
	public static ThreadLocal<WxUser> threadLocalUser = new ThreadLocal<WxUser>();

	public static void setWxUser(WxUser u) {
		threadLocalUser.set(u);
	}
	
	public static WxUser getWxUser() {
		return threadLocalUser.get();
	}
	

}
