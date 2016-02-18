package com.wang.concurrency.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CancelTester {

	public static void main(String[] args) {
		ThreadPoolExecutor tpe = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		MyTask task = new MyTask();
		Future<String> fs = tpe.submit(task);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main cancel the task");
		//使用该方法去取消任务的执行。如果参数是true，那么即使线程在运行也可以取消
		//如果是false，不可以取消。
		//不管是true还是false，isCancel和isDone始终会返回true。
		//如果任务已经被取消，或者不允许被取消，或者已经完成，则不能取消，返回值为false
		//如果任务被取消，再次调用fs.get()则会抛出CancellationExpcetion。
		//******疑问，为什么即使即取消了，而且抛出了异常后，程序依然不会停止，而是等一段时间（多久？）才停。****//
		fs.cancel(true);
// 		fs.cancel(false);
		System.out.println("fs.isCancelled() "+ fs.isCancelled());
		System.out.println("fs.isDone() "+fs.isDone());
		try {
			System.out.println("fs.get() " + fs.get());//如果使用了fs.get()则会始终等到任务结束
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();//输出：Exception in thread "main" java.util.concurrent.CancellationException
		}
		System.out.println("It's time to shutdown");
		tpe.shutdown();
		System.out.println("shutdown end ,but has been really shutdown? ");
	}

}

class MyTask implements Callable<String>{

	private int counter;
	@Override
	public String call() throws Exception {
		while(counter<10){
			System.out.println(counter + "HelloWorld");
			TimeUnit.SECONDS.sleep(1);
			counter++;
		}
		return "end";
	}
	
//	public String hello(){
//		while(true){
//			System.out.println("HelloWorld");
//		}
//	}
}
