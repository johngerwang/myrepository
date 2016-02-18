package com.wang.concurrency;

import java.util.Date;

//使用InheritableThreadLocal，在线程中再次创建线程的话，会沿用父线程中InheritableThreadLocal对象内置的值。
//而如果使用ThreadLocal，则会重新创建一个值。
public class InheritableThreadLocalTester {

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Thread safe = new Thread(new Safe());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			safe.start();
		}
	}

	private static class Safe implements Runnable {

		// 此处推荐使用static final，因为ThreadLocal对象是不会改变的，而initialValue是每一个对象会调用一次。
		//可以覆盖initialValue，设置默认值，也可以使用set()方法设置值
	//	private static InheritableThreadLocal<Date> itld = new InheritableThreadLocal<Date>();
		private static InheritableThreadLocal<Date> itld = new InheritableThreadLocal<Date>()

		{
			protected Date initialValue() {
				Date d = new Date();
				return d;
			}
		};

//		private static ThreadLocal<Date> tld = new ThreadLocal<Date>() ;

		private static ThreadLocal<Date> tld = new ThreadLocal<Date>() 
		{
			protected Date initialValue() {
				Date d = new Date();
				return d;
			}
		};
		@Override
		public void run() {
//			itld.set(new Date());
//			tld.set(new Date());
			System.out.println("InheritableThreadLocal: " + Thread.currentThread().getName() + " : " + itld.get());
			System.out.println("ThreadLocal: " + Thread.currentThread().getName() + " : " + tld.get());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			itld.set(new Date());
	//		tld.set(new Date());
//			System.out.println("InheritableThreadLocal after: " + Thread.currentThread().getName() + " : " + itld.get());
//			System.out.println("ThreadLocal after: " + Thread.currentThread().getName() + " : " + itld.get());
			Thread t = new Thread(new Runnable(){
				@Override
				public void run() {
					System.out.println("InheritableThreadLocal inner thread: " + Thread.currentThread().getName() + " : " + itld.get());
					//此处获得的是从initialValue()方法获得的默认值。因为是新的线程。
					System.out.println("ThreadLocal inner thread: " + Thread.currentThread().getName() + " : " + tld.get());
				}
			});
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}