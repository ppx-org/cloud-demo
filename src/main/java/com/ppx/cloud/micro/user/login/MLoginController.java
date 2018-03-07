package com.ppx.cloud.micro.user.login;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.micro.common.MGrantUtils;


@Controller
public class MLoginController {

	
	@PostMapping @ResponseBody
	public Map<String, Object> login(@RequestBody MLogin mLogin) {
		
		/**
		 * 1.调用wx.login()接口获取登录凭证js_code
		 * 2.调用wx.request()接口把js_code发送到服务器后台
		 * 3.在服务器后台，已知appId、secret、js_code，即可返回获取openId、session_key
		 */
		String appid = System.getProperty("wx.appid");
		String secret = System.getProperty("wx.secret");
		String jsCode = mLogin.getJsCode();
		
		/**
		 * https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
		 * 7200 秒？ 两小时？
		 * {"errcode":40163,"errmsg":"code been used, hints: [ req_id: mZ19ca0020th54 ]"}
		 * {"session_key":"TAncqMDRThNZqeEsmb3xrQ==","expires_in":7200,"openid":"oD1n60HZHWBa6ucWSdY50HNWPfq4"}
		 */
		String sessionUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
				+"&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code";
		
		HttpHeaders headers = new HttpHeaders(); 
		HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> r = restTemplate.exchange(sessionUrl, HttpMethod.GET, formEntity, String.class, "");
		
		String openid;
		String session_key = "";
		try {
			Map<?, ?> map = new ObjectMapper().readValue(r.getBody(), Map.class);
			if (map.get("errcode") != null) {
				// 微信端登录异常
				Map<String, Object> returnMap = ControllerReturn.ok(-1);
				returnMap.put("errcode", map.get("errcode"));
				returnMap.put("errmsg", map.get("errmsg"));
				return returnMap;
			}
			openid = (String)map.get("openid");
			session_key = (String)map.get("session_key");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		openid = "oD1n60HZHWBa6ucWSdY50HNWPfq4";
		session_key = "TAncqMDRThNZqeEsmb3xrQ==";
		// token
		try {
			Algorithm algorithm = Algorithm.HMAC256(MGrantUtils.getJwtPassword());
		    String token = JWT.create().withIssuedAt(new Date())
		    		.withClaim("openid", openid)
		    		.withClaim("session_key", session_key)
		    		.sign(algorithm);
		    Map<String, Object> returnMap = ControllerReturn.ok(1);
		    returnMap.put(MGrantUtils.PPXTOKEN, token);
		    return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			// 登录JWT异常
			return ControllerReturn.ok(-2);
		}
	}
	
}

