package com.ppx.cloud.grant.common;

/**
 * 分配权限上下文
 * @author dengxz
 * @date 2017年11月10日
 */
public class GrantContext {
	
	public static ThreadLocal<LoginAccount> threadLocalMerchant = new ThreadLocal<LoginAccount>();

	public static void setLoginAccount(LoginAccount mer) {
		threadLocalMerchant.set(mer);
	}
	
	public static LoginAccount getLoginAccount() {
		return threadLocalMerchant.get();
	}
	

}
