package com.ppx.cloud.micro.common;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class MGrantFilterUtils {
	
	public static WxUser getLoginUser(HttpServletRequest request) {

		String PPXTOKEN = request.getHeader("PPXTOKEN");
		String storeId = request.getHeader("storeId");
		String merchantId = request.getHeader("merchantId");
		
		// token为空,表示未登录
		if (StringUtils.isEmpty(PPXTOKEN)) {
			return null;
		}
		
		WxUser u = new WxUser();
		try {
			Algorithm algorithm = Algorithm.HMAC256(MGrantUtils.getJwtPassword());
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(PPXTOKEN);
			String openid = jwt.getClaim("openid").asString();
			String session_key = jwt.getClaim("session_key").asString();
			u.setOpenid(openid);
			u.setSessionKey(session_key);
			u.setStoreId(Integer.parseInt(storeId));
			u.setMerchantId(Integer.parseInt(merchantId));
			return u;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 登录失败
		
		u.setOpenid("oD1n60HZHWBa6ucWSdY50HNWPfq4");
		u.setStoreId(-1);
		u.setMerchantId(-1);
		
		return u;	
	}
	
	public static void main(String[] args) {
		String token = "";
		try {
		    Algorithm algorithm = Algorithm.HMAC256("JWTPASSWORDPASS");
		    token = JWT.create().withIssuedAt(new Date())
		    		.withClaim("openid", "oD1n60HZHWBa6ucWSdY50HNWPfq4")
		    		.withClaim("session_key", "session_key_value")
		    		.sign(algorithm);
			 System.out.println("......token:" + token);	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
