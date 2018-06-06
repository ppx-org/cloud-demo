package com.ppx.cloud.demo.module.thread;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ParentThread implements Runnable {

	private CountDownLatch downLatch = null;
	private int messageSize;

	public ParentThread(int messageSize) {
		this.messageSize = messageSize;
	}

	public CountDownLatch getDownLatch() {
		return downLatch;
	}

	public void setDownLatch(CountDownLatch downLatch) {
		this.downLatch = downLatch;
	}

	@Override
	public void run() {
		try {
			
			this.downLatch.await();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
