package com.wang.concurrency.rejected.execution.handler;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//给ThreadPoolExecutor的setRejectedExecutionHandler方法设置一个RejectedExecutionHandler接口（需要实现）
//当ThreadPoolExecutor shutdown后，但是又给该ThreadPoolExecutor对象发送任务处理时，会调用该"拒绝执行处理器"来做相应的处理。
//当ThreadPoolExecutor执行任务时，首先会判断是否已经shutdown了，如果是，则首先判断是否有setRejectedExecutionHandler，
//如果有，那么处理。否则抛出RejectedExecutionException
public class RejectedExecutionHandlerTester {

	public static void main(String[] args) {

		ThreadPoolExecutor tpe = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		tpe.setRejectedExecutionHandler(new MyRejectedExecutionHandler());
		tpe.execute(new Task("Task1"));
		tpe.shutdown();
		tpe.submit(new Task("Task2"));
	}

}

class MyRejectedExecutionHandler implements RejectedExecutionHandler{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		//注意，Runnable的r对象是一个FutureTask对象。并不是Task对象。
		System.out.println("MyRejectedExecutionHandler: reject the task: " + r.toString());
		System.out.println("MyRejectedExecutionHandler: reject the executor: " +executor.toString());
		System.out.println("MyRejectedExecutionHandler: executor is terminating: " +executor.isTerminating());
		System.out.println("MyRejectedExecutionHandler: executor is terminated: " +executor.isTerminated());
	}
}

class Task implements Runnable{

	private String name;
	public Task(String name){
		this.name =name;
	}
	@Override
	public void run() {
		System.out.println("Task: starting");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Task: ended");
	}
	
	public String getName(){
		return name;
	}
	public String toString(){
		return name;
	}
	
}