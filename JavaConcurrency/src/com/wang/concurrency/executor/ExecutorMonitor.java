package com.wang.concurrency.executor;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorMonitor {

	public static void main(String[] args) {
		ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();

		for (int i = 0; i < 10; i++) {
			Tasker t = new Tasker(new Random().nextInt(10000));
			tpe.execute(t);
		}

		for (int i = 0; i < 5; i++) {
			showlog(tpe);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tpe.shutdown();
		System.out.println("after shutdown");
		for (int i = 0; i < 5; i++) {
			showlog(tpe);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			tpe.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main End");
	}

	public static void showlog(ThreadPoolExecutor tpe) {
		System.out.println("========ThreadPoolExecutor logging============");
		System.out.println("tpe.getCorePoolSize(): " + tpe.getCorePoolSize());//执行器不执行任务时，内部的最小线程数
		System.out.println("tpe.getPoolSize(): " + tpe.getPoolSize());//内部线程池的实际大小
		System.out.println("tpe.getCompletedTaskCount(): " + tpe.getCompletedTaskCount());
		System.out.println("tpe.getTaskCount(): "+tpe.getTaskCount());//所有的任务
		System.out.println("tpe.getActiveCount(): " + tpe.getActiveCount());
		System.out.println("tpe.getQueue(): " + tpe.getQueue());
		System.out.println("tpe.isShutdown(): " + tpe.isShutdown());
		System.out.println("tpe.isTerminated(): " + tpe.isTerminated());
		System.out.println("tpe.isTerminating(): " + tpe.isTerminating());
	}

}

class Tasker implements Runnable {

	private int delayTime;

	public Tasker(int delayTime) {
		this.delayTime = delayTime;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " started");
		try {
			TimeUnit.MILLISECONDS.sleep(delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " ended");

	}

}