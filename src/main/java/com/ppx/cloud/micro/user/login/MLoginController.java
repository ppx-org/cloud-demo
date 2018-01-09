package com.ppx.cloud.micro.user.login;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.micro.common.MGrantUtils;


@Controller
public class MLoginController {
	
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> login() {
		
		
		System.out.println("xxxxxxxxxxxxxout:------------------begin:001");
		// 
		String appid = "wxe2d27ee638a5cb7a";
		String secret = "5a3e84b9d19c79d75db597583e3a74d5";
		String jsCode = "013wHxWi2XLk4J0HEFWi2Gx9Wi2wHxWQ";
		
		
		
		
		// https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
		String sessionUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
				+"&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code";
		
		HttpHeaders headers = new HttpHeaders();  
		HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> r = restTemplate.exchange(sessionUrl, HttpMethod.GET, formEntity, String.class, "");
		
		System.out.println("xxxxxxxxxxxxx-------out:" + r.getHeaders());
		System.out.println("xxxxxxxxxxxxx-------out:" + r.getBody());
		
		
		
		
		String returnJson = r.getBody();
		
		
		String openid;
		String session_key = "";
		
		try {
			Map<?, ?> map = new ObjectMapper().readValue(returnJson, Map.class);
			System.out.println("xxxxxxxxxmap.......:" + map.get("errmsg"));
			
			if (map.get("errcode") != null) {
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
		
		
		// 7200 秒？ 两小时？
		// {"errcode":40163,"errmsg":"code been used, hints: [ req_id: mZ19ca0020th54 ]"}
		// {"session_key":"TAncqMDRThNZqeEsmb3xrQ==","expires_in":7200,"openid":"oD1n60HZHWBa6ucWSdY50HNWPfq4"}
		
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
			// 登录异常
			return ControllerReturn.ok(-1);
		}
	}
	
}

