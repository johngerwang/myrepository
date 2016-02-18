package com.wang.concurrency;

import static java.lang.System.out;


/**
 * 
 * 在主线程中测试InheritableThreadLocal和ThreadLocal。
 * 初始值都为abc，通过在子线程中观察InheritableThreadLocal和和ThreadLocal的区别。
 *输出结果
 *Thread[main,5,main] after set ,a
 *Thread[main,5,main] after set ,a
 *Thread[ThreadLocal,5,main] ThreadLocal initial:abc
 *sub Thread[ThreadLocal,5,main] before set ,abc
 *sub Thread[ThreadLocal,5,main] after set ,b
 *
 *
 *sub Thread[InheritableThreadLocal,5,main] before set ,a
 *sub Thread[InheritableThreadLocal,5,main] after set ,b
 *Thread[main,5,main] final ,a
 *Thread[main,5,main] final ,a
 */

public class TestInheritableThreadLocal {
	
	public static void main(String[] args){
		testThreadLocal();
		testInheritableThreadLocal() ;
	}
	

	public static void testThreadLocal() {
		final ThreadLocal<String> local = new ThreadLocal<String>(){
			protected String initialValue(){
				out.println(Thread.currentThread() + " ThreadLocal initial:abc");
				return "abc";
			}
		};
		work(local,"ThreadLocal");
	}
	
	public static void testInheritableThreadLocal() {
		final ThreadLocal<String> local = new InheritableThreadLocal<String>(){
			protected String initialValue(){
				out.println(Thread.currentThread() + " InheritableThreadLocal initial:abc");
				return "abc";
			}
		};
		work(local,"InheritableThreadLocal");
	}
	
	private static void work(final ThreadLocal<String> local,String name) {
		local.set("a");
		out.println(Thread.currentThread() + " after set ," + local.get());
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				//如果是ThreadLocal，那么此时的值依然是abc，即每次都用全新（初始）值
				//如果是InheritableThreadLocal，那么此时的值是a，即继承了之前设置的值
				out.println("sub "+Thread.currentThread() + " before set ," + local.get());
				local.set("b");
				
				out.println("sub "+ Thread.currentThread() + " after set ,"+ local.get());
			}
		},name);
		
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		out.println(Thread.currentThread() + " final ," + local.get());
		//此处输出：Thread[main,5,main] final ,a，
		//即如果是InheritableThreadLocal，主线程不会使用子线程的值，而子线程会使用主线程的值。
		//而如果是ThreadLocal，那么父子线程互不干涉。
	}
}
