package com.ppx.cloud.demo.module.test;

public class Test {
	
	
	public static void main(String[] args) {
		System.out.println("-----------1");
		try {
			Del.test();
			//throw new NoClassDefFoundError("e");
		} catch (Exception e) {
			System.out.println("-----------2");
		} finally {
			System.out.println("-----------3");
		}
		System.out.println("-----------4");
	}
	
	


}






