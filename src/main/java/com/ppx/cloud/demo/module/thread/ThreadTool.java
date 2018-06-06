package com.ppx.cloud.demo.module.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTool {
	
	private final static ExecutorService jober = Executors.newFixedThreadPool(8);
	
	private final static ThreadTool instance = new ThreadTool();
	
	private ThreadTool() {
		
	}
	
	public static ThreadTool getInstance(){
		return instance;
	}
	
	public void runCal(List<MpMessage> messageList) {
		try {
			ParentThread parent = new ParentThread(messageList.size());
			
			int threadSize = messageList.size() >= 20 ? 8:1;
			CountDownLatch countDown = new CountDownLatch(threadSize);
			
			int childLen = messageList.size() / threadSize;
			for (int i = 0; i < threadSize; i++) {
				int fromIndex = childLen * i;
				int toIndex = childLen * (i + 1);
				if (i == (threadSize - 1)) {
					toIndex = messageList.size();
				}
				
				List<MpMessage> subList = messageList.subList(fromIndex, toIndex);
				ChildThread child = new ChildThread(countDown, subList);
				jober.execute(child);
			}
			
			parent.setDownLatch(countDown);
			parent.run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != jober) {
				jober.shutdownNow(); //强制阻断所有线程（可能存在线程还在跑，但是由于超时断开了）
			}
		}
		
		
		
		
		
		
	}
	
}
