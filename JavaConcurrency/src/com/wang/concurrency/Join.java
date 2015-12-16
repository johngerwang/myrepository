package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Join {

	public static void main(String[] args) throws InterruptedException {
		System.out.println(Join.class + " started:" + new Date());
		Thread dsl = new Thread(new Join().new DataSourceLoader());
		Thread nwc = new Thread(new Join().new NWConnection());
		dsl.start();
		nwc.start();
		dsl.join(); //使主线程(main)挂起，等等dsl线程执行结束。
		nwc.join();//使主线程(main)挂起，等等nwc线程执行结束。
		System.out.println(Join.class + " ended:" + new Date());
	}

	class DataSourceLoader implements Runnable {

		@Override
		public void run() {
			System.out.println("DataSourceLoader started :" + new Date());
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("DataSourceLoader ended: " + new Date());
		}

	}

	class NWConnection implements Runnable {

		@Override
		public void run() {
			System.out.println("NWConnection started: " + new Date());
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("NWConnection ended: " + new Date());
		}

	}
}
