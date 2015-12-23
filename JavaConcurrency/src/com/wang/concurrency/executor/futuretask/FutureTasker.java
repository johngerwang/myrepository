package com.wang.concurrency.executor.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

//当Task停止时回调FutureTask的done()方法，
public class FutureTasker {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService es = Executors.newCachedThreadPool();
		MyFutureTask[] mfts = new MyFutureTask[5];
		for (int i = 0; i < 5; i++) {
			//FutureTask需要包装Runnable对象，FutureTask也是一个Runnable对象。
			mfts[i] = new MyFutureTask(new MyTask("task$" + i));
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			es.submit(mfts[i]);

		}
		for (int i = 0; i < 5; i++) {
			System.out.println("===========before cancel MyTask is done " + mfts[i].isDone() + "===========");
			mfts[i].cancel(true);
		}
		for (int i = 0; i < 5; i++) {
			if (!mfts[i].isCancelled()) {
				System.out.println("MyTask Result " + mfts[i].get()); // 如果取消后再去调用，则抛出java.util.concurrent.CancellationException
			}
		}
		es.shutdown();
	}

}

class MyFutureTask extends FutureTask<String> {

	private String name;

	public MyFutureTask(Callable<String> callable) {
		super(callable);
		this.name = ((MyTask) callable).getName();
	}

	protected void done() {
		if (this.isCancelled()) {
			System.out.println("MyFutureTask is cancelled " + name);
		} else {
			System.out.println("MyFutureTask is done " + name);
		}
	}

}

class MyTask implements Callable<String> {

	private String name;

	public MyTask(String name) {
		this.name = name;
	}

	@Override
	public String call() throws Exception {
		TimeUnit.SECONDS.sleep(1);
		return "I am " + name;
	}

	public String getName() {
		return name;
	}

}