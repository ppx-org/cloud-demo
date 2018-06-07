package com.ppx.cloud.demo.module.okr;

public class Test {
	public static void main(String[] args) {
		long t1 = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			String s = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" 
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBBBB"
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCC"
				+ "DDDDDDDDDDDDDDDDDDDDDDDDDDDDD";
			//go(s);
		}
		System.out.println("out1:" + (System.nanoTime() - t1));
		
		long t2 = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			StringBuffer s = new StringBuffer("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			s.append("BBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
			s.append("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
			s.append("DDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
			//go2(s);
		}
		System.out.println("out2:" + (System.nanoTime() - t2));
		
		long t3 = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			StringBuilder s = new StringBuilder("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			s.append("BBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
			s.append("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
			s.append("DDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
			//go3(s);
		}
		System.out.println("out3:" + (System.nanoTime() - t3));
	}
	
	public static void go(String s) {
		s = s + "END....................................";
	}
	
	public static void go2(StringBuffer s) {
		s = s.append("END....................................");
	}
	
	public static void go3(StringBuilder s) {
		s = s.append("END....................................");
	}
}
