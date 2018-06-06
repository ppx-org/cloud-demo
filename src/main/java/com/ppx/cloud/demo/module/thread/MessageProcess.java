package com.ppx.cloud.demo.module.thread;

import java.util.List;

public class MessageProcess {
	
	public static void sendMessageList(List<MpMessage> messageList) {
		
		
		for (MpMessage mpMessage : messageList) {
			System.out.println("" + mpMessage.getId());	
			if ("8".equals(mpMessage.getId())) {
				int i = 1 / 0;
			}
		}
		
		
	}
}
