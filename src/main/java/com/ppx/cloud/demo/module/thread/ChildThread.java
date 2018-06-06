package com.ppx.cloud.demo.module.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ChildThread implements Runnable {
	
	private CountDownLatch count = null;
	private List<MpMessage> messageList = null;
	
	public ChildThread(CountDownLatch count, List<MpMessage> messageList) {
		this.count = count;
		this.messageList = messageList;
		
	}
	
	@Override
	public void run() {
		try {
			System.out.println("thread-name:" + Thread.currentThread().getName());
			MessageProcess.sendMessageList(messageList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			count.countDown();
		}
	}
	

}
