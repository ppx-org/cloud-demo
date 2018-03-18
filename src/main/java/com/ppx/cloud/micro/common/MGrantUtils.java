package com.ppx.cloud.micro.common;

public class MGrantUtils {
	
	public static String PPX_TOKEN = "PPX_TOKEN";
	
	
	public static String getJwtPassword() {
		return System.getProperty("jwt.password") + "PASS";
	}
}
