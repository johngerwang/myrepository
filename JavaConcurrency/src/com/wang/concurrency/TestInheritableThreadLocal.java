package com.wang.concurrency;

import static java.lang.System.out;

public class TestInheritableThreadLocal {
	
	public static void main(String[] args){
		testThreadLocal();
		testInheritableThreadLocal() ;
	}
	

	public static void testThreadLocal() {
		final ThreadLocal<String> local = new ThreadLocal<String>(){
			protected String initialValue(){
				return "abc";
			}
		};
		work(local);
	}
	
	public static void testInheritableThreadLocal() {
		final ThreadLocal<String> local = new InheritableThreadLocal<String>(){
			protected String initialValue(){
				return "abc";
			}
		};
		work(local);
	}
	
	private static void work(final ThreadLocal<String> local) {
		local.set("a");
		out.println(Thread.currentThread() + "," + local.get());
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				out.println(Thread.currentThread() + "," + local.get());
				local.set("b");
				out.println(Thread.currentThread() + "," + local.get());
			}
		});
		
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		out.println(Thread.currentThread() + "," + local.get());
	}
}
