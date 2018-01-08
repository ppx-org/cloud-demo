package com.ppx.cloud.micro.common;

public class MGrantUtils {
	
	public static String PPXTOKEN = "PPXTOKEN";
	
	
	public static String getJwtPassword() {
		return System.getProperty("jwt.password") + "PASS";
	}
}
