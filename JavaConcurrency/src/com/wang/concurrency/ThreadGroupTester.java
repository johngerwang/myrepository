package com.wang.concurrency;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 使用线程组，Thread et = new Thread(ThreadGroup,Runnable)
 * 是线程可以一个整体来控制，比如设置一个线程组的遗产捕捉后，那么所有所属与这个线程组的线程的异常都可以被捕获
 * 
 */
public class ThreadGroupTester {

	public static void main(String[] args) {
		testUncaughtException();
			}

	public static void testUncaughtException(){
		MyThreadGroup mtg = new MyThreadGroup("expcetion");
		Thread et = new Thread(mtg,new Runnable(){//将线程加入线程组。
			@Override
			public void run() {
				while(true){
					int result = 100/(int)(new Random().nextDouble()*100);
					System.out.println(Thread.currentThread().getName()+" result is :"+result);
					if(Thread.interrupted()){
						System.out.println(Thread.currentThread().getName()+" is interrupted");
						return;
					}
				}
			}
		});
		et.start();
	}
	public void testTGT(){
		ThreadGroup tg = new ThreadGroup("footask");
		for (int i = 0; i < 10; i++) {
			Thread footask = new Thread(tg, new FooTask());
			footask.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Current Active Thread Number is : " + tg.activeCount());
		System.out.print("Info of Current Thread Group is: ");
		tg.list();
		Thread[] threads = new Thread[tg.activeCount()];
		tg.enumerate(threads);
		for (int i = 0; i < threads.length; i++) {
			System.out.println(threads[i].getName() + " : " + threads[i].getState());
		}

		//只要有一个线程结束，就中断整个线程组
		while (tg.activeCount() > 9) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tg.interrupt();

	}
}

class FooTask implements Runnable {

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		// System.out.println(name+ " is started");
		try {
			TimeUnit.SECONDS.sleep(new Random().nextLong());
		} catch (InterruptedException e) {
			System.out.println(name + " is interrupted");
		}
		// System.out.println(name+" is ended");
	}
}

class MyThreadGroup extends ThreadGroup{

	public MyThreadGroup(String name) {
		super(name);
	}
	//该非捕获异常的优先级为2.线程的defaultExceptionHandler设置的优先级为3.
	//优先级为1的是，线程自身的uncaughtException。参考ThreadCaughtException类
	@Override
	public void uncaughtException(Thread t,Throwable e){
		System.out.println(t.getName()+" throws exception: "+e.getMessage());
		this.interrupt();
	}
	
}