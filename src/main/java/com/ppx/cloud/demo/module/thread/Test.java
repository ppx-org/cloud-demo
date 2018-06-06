package com.ppx.cloud.demo.module.thread;

import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		
		System.out.println("---------------------begin.............:");
		
		List<MpMessage> list = new ArrayList<MpMessage>();
		
		for (int i = 0; i < 50; i++) {
			MpMessage m = new MpMessage();
			m.setId("" + i);
			list.add(m);
		}
		
		//ThreadTool.getInstance().runCal(list);
		int s = 1;s=(++s)*(++s)*(++s);
		
		System.out.println("---------------------end.............:" + s);
	}
}
