package com.wang.concurrency.executor.schedule.threadpoolexecutor;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class CallableTask implements Callable<String>{

	@Override
	public String call() throws Exception {
		System.out.println("CallableTask: Hello World "+new Date());
		return "Hello World "+new Date();
	}
	
}

public class ScheduleThreadPoolExecutorTester{
	public static void main(String[] args){
		//scheduleAtFixedRate();
		schedule();
	}
	
	public static void schedule(){
		//ScheduledThreadPoolExecutor->ScheduledExecutorService(接口）&ThreadPoolExecutor(类)
		ScheduledThreadPoolExecutor stpe = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);
		
		for(int i=0;i<5;i++){
			CallableTask t = new CallableTask();
			stpe.schedule(t, 1+i, TimeUnit.SECONDS);
		}
		stpe.shutdown();

		try {
			stpe.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	 
	}
	
	/**
	 *  
	 */ //   */之间不能有空格，有的话格式错误
	public static void scheduleAtFixedRate(){
		//ScheduledThreadPoolExecutor->ScheduledExecutorService(接口）&ThreadPoolExecutor(类)
		ScheduledThreadPoolExecutor stpe = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);
		
			RunnableTask t = new RunnableTask();
			//第3个参数的意思是，每次开始的时间的间隔。如果线程执行时间大于该参数，则会出现2个线程同时执行的情况
			//ScheduledFuture<?> 是一个泛化接口，因为runnable并没有泛化参数。
			//只能接受Runnable接口，不能是Callable
			ScheduledFuture<?> result = stpe.scheduleAtFixedRate(t, 1, 2, TimeUnit.SECONDS);
			
			//与上面的方法唯一的区别是第3个参数的意思不同。此处是指一轮线程结束后距离开一轮开始运行的时间。
			//stpe.scheduleWithFixedDelay(command, initialDelay, delay, unit)
			
			for(int i =0;i<10;i++){
				System.out.println(result.getDelay(TimeUnit.MILLISECONDS));
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stpe.shutdown();
			try {
				TimeUnit.MILLISECONDS.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("scheduleAtFixedRate end");
	}
}

class RunnableTask implements Runnable{

	@Override
	public void run() {
		System.out.println("RunnableTask: Hello World "+new Date());
	}
	
}