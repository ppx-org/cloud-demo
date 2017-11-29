package com.ppx.cloud.grant.common;

import com.ppx.cloud.common.util.MD5Utils;

public class GrantUtils {
	
	public static String PPXTOKEN = "PPXTOKEN";
	
	
	public static String getJwtPassword() {
		return System.getProperty("jwt.password") + "PASS";
	}
	
	public static String getMD5Password(String p) {
		return MD5Utils.getMD5(p + "PASS");
	}
}
