package com.ppx.cloud.micro.common;

/**
 * 分配权限上下文
 * @author dengxz
 * @date 2017年11月10日
 */
public class MGrantContext {
	
	public static ThreadLocal<LoginUser> threadLocalUser = new ThreadLocal<LoginUser>();

	public static void setLoginUser(LoginUser u) {
		threadLocalUser.set(u);
	}
	
	public static LoginUser getLoginUser() {
		return threadLocalUser.get();
	}
	

}
