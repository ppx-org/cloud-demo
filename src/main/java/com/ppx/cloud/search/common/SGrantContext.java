package com.ppx.cloud.search.common;

/**
 * 分配权限上下文
 * @author dengxz
 * @date 2017年11月10日
 */
public class SGrantContext {
	
	public static ThreadLocal<Session> threadLocalSession = new ThreadLocal<Session>();

	public static void setSession(Session u) {
		threadLocalSession.set(u);
	}
	
	public static Session getSession() {
		return threadLocalSession.get();
	}
	

}
