package com.ppx.cloud.grant.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppx.cloud.common.util.CookieUtils;
import com.ppx.cloud.grant.common.GrantUtils;
import com.ppx.cloud.grant.service.LoginService;

/**
 * 登录
 * @author dengxz
 * @date 2017年11月10日
 */
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	private final static String VALIDATE_TOKEN_PASSWORK = "FSSBBA";
	private final static String VALIDATE_TOKEN_NAME = "FSSBBIl1UgzbN7N443T";
	private final static String VALIDATE_JS_PASSWORK = "DhefwqGPrzGxEp9hPaoag";
	
	private void createValidateToken(ModelAndView mv, HttpServletResponse response) throws Exception {
		// 产生验证token到页面和cookie，以便验证合法性，支持分布式
		String v = UUID.randomUUID().toString();
		mv.addObject("v", v);
		Algorithm algorithm = Algorithm.HMAC256(VALIDATE_TOKEN_PASSWORK);
		String token = JWT.create().withClaim("v", v).sign(algorithm);
		CookieUtils.setCookie(response, VALIDATE_TOKEN_NAME, token);
	}
	
	@GetMapping
    public ModelAndView loginIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		createValidateToken(mv, response);
		
		// 清空登录cookie,退出时可调用试方法
		CookieUtils.cleanCookie(response, GrantUtils.PPXTOKEN);
		return mv;
	}

	
	@PostMapping @ResponseBody
	public Map<String, Object> doLogin(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam String a, @RequestParam String p, @RequestParam String v) {

		// 验证"验证token",不合法就返回403 
		try {
			Map<String, String> cookieMap = CookieUtils.getCookieMap(request);
			Algorithm algorithm = Algorithm.HMAC256(VALIDATE_TOKEN_PASSWORK);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(cookieMap.get(VALIDATE_TOKEN_NAME));
			String cookieV = jwt.getClaim("v").asString();
			cookieV = cookieV.substring(cookieV.length() - 21);
			StringBuilder r = new StringBuilder();
			for (int i = 0; i < cookieV.length(); i++) {
				r.append(cookieV.charAt(i) + VALIDATE_JS_PASSWORK.charAt(i));
			}
			
			if (!v.equals(r.toString())) {
				response.setStatus(403);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(403);
			return null;
		}
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> accoutMap = loginService.getLoginAccount(a, p);
		if (accoutMap == null) {
			// 账号或密码不存在
			returnMap.put("code", "-1");
		}
		else {
			// 帐号和密码正确，则在cookie上生成一个token
			String token = "";
			try {
			    Algorithm algorithm = Algorithm.HMAC256(GrantUtils.getJwtPassword());
			    token = JWT.create().withIssuedAt(new Date())
			    		.withClaim("ACCOUNT_ID", (Integer)accoutMap.get("ACCOUNT_ID"))
			    		.withClaim("LOGIN_ACCOUNT", (String)accoutMap.get("LOGIN_ACCOUNT"))
			    		.withClaim("MERCHANT_ID", (Integer)accoutMap.get("MERCHANT_ID"))
			    		.withClaim("MERCHANT_NAME", (String)accoutMap.get("MERCHANT_NAME"))
			    		.sign(algorithm);
			    CookieUtils.setCookie(response, GrantUtils.PPXTOKEN, token);
			    // 登录成功
				returnMap.put("code", "1");				
			} catch (Exception exception){
				exception.printStackTrace();
				// 登录异常
				returnMap.put("code", "-2");
				return returnMap;
			}
		}
		return returnMap;
	}
}
