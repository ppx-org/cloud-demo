package com.ppx.cloud.grant.filter;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppx.cloud.common.config.PropertiesConfig;
import com.ppx.cloud.common.exception.custom.PermissionUrlException;
import com.ppx.cloud.common.util.CookieUtils;
import com.ppx.cloud.grant.common.GrantUtils;
import com.ppx.cloud.grant.common.LoginAccount;

public class GrantFilterUtils {
	
	/**
	 * 获取tocken里的用户信息
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static LoginAccount getLoginAccout(HttpServletRequest request, String uri) throws Exception {
		
		// 从cookie中取得token
		String token = CookieUtils.getCookieMap(request).get(GrantUtils.PPXTOKEN);		
	
		// token为空,表示未登录
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		
		Algorithm algorithm = Algorithm.HMAC256(GrantUtils.getJwtPassword());
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT jwt = verifier.verify(token);
		Integer accountId = jwt.getClaim("ACCOUNT_ID").asInt();
		String loginAccount = jwt.getClaim("LOGIN_ACCOUNT").asString();
		Integer merchantId = jwt.getClaim("MERCHANT_ID").asInt();
		String merchantName = jwt.getClaim("MERCHANT_NAME").asString();
				
		LoginAccount account = new LoginAccount();
		account.setAccountId(accountId);
		account.setLoginAccount(loginAccount);
		account.setMerchantId(merchantId);
		account.setMerchantName(merchantName);
		
		// 超级管理员或所有用户进入/index/(菜单、修改密码等)的不拦截
		if ("admin".equals(loginAccount) || uri.startsWith("/index/")) {
			return account;
		}
		
		// 权限过滤服务 >>>>>>>>>>>>>>>>
		GrantFilterService filterService = (GrantFilterService)PropertiesConfig.app.getBean("grantFilterService");
	
		//  大中小权限 /* /uriItem/* /uriItem1/uriItem2 /uriItem1/uriItem2?q=1
		List<String> testUriList = new ArrayList<String>();
		
		String queryString = request.getQueryString();
		if (!StringUtils.isEmpty(queryString)) {
			// 只支持带一参数的
			testUriList.add(uri + "?" + queryString.split("&")[0]);
		}
		testUriList.add(uri);
		testUriList.add("/" + uri.split("/")[1] + "/*");
		testUriList.add("/*");
		
		boolean missUri = true;
		for (String testUri : testUriList) {
			// 取得URI对应的index
			Integer index = filterService.getIndexFromUri(testUri);
			if (index == null) {
				continue;
			}

			// 判断帐号是否有uri的权限
			BitSet grantBitset = filterService.getMerchantResBitSet(accountId);
			
			if (!account.isMainAccont()) {
				// 当子帐号登录时，必须判断商户权限，防止撤消商户权限时，子帐号还有权限
				BitSet merchantGrantBitset = filterService.getMerchantResBitSet(account.getMerchantId());
				if (!merchantGrantBitset.get(index)) {
					continue;
				}
			}
			
			if (grantBitset.get(index)) {
				// uri合法访问, 通过request.setAttribute设置操作权限 传入真实URI
				List<Map> opList = filterService.getOpUri(uri);
				for (Map map : opList) {
					List<Integer> indexList = (List<Integer>)map.get("uriIndex");
					for (Integer uriIndex: indexList) {
						if (grantBitset.get(uriIndex)) {
							// /test/saveTest改名成_test_saveTest
							setOpRequest(request, (List<String>)map.get("uri"));
						}
					}
				}
				return account;
			}
			missUri = false;
		}

		if (missUri) {
			// 从mongodb找不到对应uri,资源对应的uri找不到
			throw new PermissionUrlException("Unauthorized.miss uri:" + uri);
		} else {
			throw new PermissionUrlException("Unauthorized.forbiddens:" + uri);
		}
	}
	
	private static void setOpRequest(HttpServletRequest request, List<String> uriList) {
		for (String uri : uriList) {
			request.setAttribute(uri.replace("/", "_"), true);
		}
	}
}
