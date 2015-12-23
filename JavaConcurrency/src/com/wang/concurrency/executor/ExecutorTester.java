package com.wang.concurrency.executor;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/*
 * 
 * 使用ThreadPoolExecutor线程池执行器来创建，执行线程。
 * 一般使用Executors接口的newCachedThreadPool来创建该执行器，但是需要强转，因为返回的类型是ExecutorService接口
 * 		(ThreadPoolExecutor)Executors.newCachedThreadPool();
 * 继承关系是：ThreadPoolExecutor->AbstractExecutorService(抽象类)->ExecutorService(接口)->Executor(接口)。Executors是辅助类。
 * */
public class ExecutorTester {

	public static void main(String[] args) {
		Server server = new Server();
		for(int i=0;i<100;i++){
			Task task = new Task(new Date(),"task"+i);
			server.execute(task);
		}
		server.shutdown();
	}

}

class Task implements Runnable{

	private Date createDate;
	private String name;
	
	public Task(Date createDate,String name){
		this.createDate = createDate;
		this.name = name;
	}
	public String toString(){
		return name +": created at "+ createDate;
	}
	@Override
	public void run() {
		System.out.println(this);
		System.out.println(name +": started at "+ new Date() );
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name +": ended at "+ new Date() );
	}
}

class Server{
	
	private ThreadPoolExecutor executor;
	
	public Server(){
		//有Task进来就新建线程，有多少做多少，这样可能会导致性能下降。
//		executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		//创建10个线程的池子，当超过10个需要执行的task时，其余就等待。
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
		//创建只有一个线程的池子。
//		executor = (ThreadPoolExecutor)Executors.newSingleThreadExecutor();
		
	}
	
	public void execute(Task task){
		System.out.println("======New Task Arrived at: " +new Date()+"======");
		System.out.println("Server: Task Count: " + executor.getTaskCount());
		System.out.println("Executing Size: "+executor.getActiveCount());
		System.out.println("Actual Size is: "+executor.getPoolSize());//实际在线程池中存在的线程数量，最大为与设置的10。
		System.out.println("executor.getCompletedTaskCount(): " + executor.getCompletedTaskCount());
		System.out.println("executor.getCorePoolSize(): "+executor.getCorePoolSize());
		System.out.println("executor.getLargestPoolSize(): "+executor.getLargestPoolSize());
		System.out.println("executor.getPoolSize(): "+executor.getPoolSize());
		executor.execute(task);
		System.out.println("======New Task Completed at: " +new Date()+"=====");
	}
	
	public void shutdown(){
		executor.shutdown();
	}
}
