package com.ppx.cloud.demo.module.test;

public class Test {

	public static void mainX(String[] args) {
		System.out.println("-----------1");
		try {
			Del.test();
			throw new NoClassDefFoundError("e");
		} catch (Exception e) {
			System.out.println("-----------2");
		} finally {
			System.out.println("-----------3");
		}
		System.out.println("-----------4");
	}

	public static void main(String[] args) {
		// 0 == ''; true
		/*
			var orderNo = 201808083343412459;
			alert(orderNo == 201808083343412456);
		 */
		Integer i1 = 129;
		Integer i2 = 129;
		System.out.println("out:" + (i1 == 129));
		
		
		System.out.println("--------------01--------------02--------------03--------------ddddddddddddddddddddddddddd");
		
	}

}
