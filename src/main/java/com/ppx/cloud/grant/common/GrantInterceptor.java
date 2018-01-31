package com.ppx.cloud.grant.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.common.controller.ControllerContext;
import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.filter.GrantFilterUtils;
import com.ppx.cloud.micro.common.WxUser;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.MGrantFilterUtils;


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

		
		// wx micro
		if (uri.startsWith("/M")) {
			if (uri.equals("/MLogin/login") || uri.startsWith("/MRequest/") || uri.startsWith("/MQuery/pcQuery")) {
				return true;
			}
			
			WxUser u = MGrantFilterUtils.getLoginUser(request);
			if (u == null) {
				// 未登录
				
				ControllerReturn.returnErrorJson(response, -9, "no login");
				
				return false;
			}
			
			MGrantContext.setWxUser(u);
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