package com.ppx.cloud.grant.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerContext;
import com.ppx.cloud.grant.filter.GrantFilterUtils;


/**
 * 
 * @author dengxz
 * @date 2017年11月4日
 */
public class GrantInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI().replace(contextPath, "");

		System.out.println("xxxxxxxxxxxxxxxxxxxx..........uri:" + uri);
		// small
		if (uri.startsWith("/S")) {
			return true;
		}
		
		
		// 不拦截登录页
		if (uri.startsWith("/login/")) {
			return true;
		}
		
		LoginAccount account = GrantFilterUtils.getLoginAccout(request, uri);
		if (account == null) {
			// 跳转到登录页面
			response.sendRedirect(contextPath + "/login/loginIndex");
			return false;
		} else {
			ControllerContext.getAccessLog().setUserId(account.getAccountId() + "");
			GrantContext.setLoginAccount(account);
		}

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}