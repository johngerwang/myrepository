package com.wang.concurrency.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//在执行器中执行并返回结果。
public class FutureAndCallable {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		Random rm = new Random();
		for (int i = 0; i < 10; i++) {
			CallableTask t = new CallableTask(rm.nextInt(10));
			// 通过submit方法将任务提交给线程执行器，执行完的结果会以Future的形式返回。
			//一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行!!!!已经开始执行了!!!!。
			//返回的Future对象一定不为空，等线程执行完毕，再设置Future对象的实际的值，如本例的Integer类型的值
			Future<Integer> result = tpe.submit(t);//此处会立刻返回一个future对象了，而实际的值未必有，得看线程的执行状况。
			System.out.println("=======Task#" + i + " is done: " + result.isDone()+"====");
			list.add(result);
		}
		do {
			for (int i = 0; i < 10; i++) {
				Future<Integer> f = list.get(i);
				System.out.println("Task#" + i + " is done: " + f.isDone());
			}
			try {
				TimeUnit.MICROSECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (tpe.getCompletedTaskCount() < list.size());

		for (int i = 0; i < list.size(); i++) {
			Future<Integer> f = list.get(i);//会一直等待，直到call方法有返回结果。
			try {
				System.out.println("Task#" + i + "result: "+ f.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		//=================此处开始为了测试submit(rt, result)的result===========
		List<Future<String>> lf = new ArrayList<Future<String>>();
		for (int i = 0; i < 10; i++) {
			RunTask rt = new RunTask();
			String result = "OK";
			Future<String> f = tpe.submit(rt, result);//当成功执行线程的动作时，返回result。失败时怎么办？
			lf.add(f);
		}
		TimeUnit.SECONDS.sleep(10);
		for (int i = 0; i < 10; i++) {
			try {
				String result = lf.get(i).get();//会一直等待，直到call方法有返回结果。
				System.out.println("tpe.submit(rt, result): "+ result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		tpe.shutdown();
	}
}

class CallableTask implements Callable<Integer>{

	private Integer number;
	
	public CallableTask(Integer number){
		this.number = number;
	}
	
	//call方法中如果有异常抛出，会被get()方法捕获。
	@Override
	public Integer call() throws Exception {
		int result =1;
		if(number==0||number==1){
			 result =1;
		}else{
			for(int i=2;i<=	number;i++){
				result*=i;
			}
		}
		System.out.println(Thread.currentThread().getName() +": result:  "+result);
		return result;
	}
}

class RunTask implements Runnable{

	private static int counter;
	@Override
	public void run() {
		if((counter++)%2==0){
			System.out.println("OVEROVEROVEROVEROVEROVEROVEROVER");
		}else{
			Integer.parseInt("TTTT");
		}
		
	}
	
}
