package com.wang.concurrency.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTester {

	public static void main(String[] args) {

		
//        ExecutorService executorService = Executors.newCachedThreadPool();  
//
//        for(int i=0;i<10;i++){
//        	executorService.execute(new Job(new PrintQueue()));
//        }
//        executorService.shutdown();
		PrintQueue pq = new PrintQueue();
        Thread[] threads = new Thread[10];
        for(int i=0;i<10;i++){
        	threads[i] = new Thread(new Job(pq));
        }
        for(int i=0;i<10;i++){
        	threads[i].start();
        }
	}
}


class PrintQueue{
	
	private final Semaphore sh ;
	
	public PrintQueue(){
		this.sh = new Semaphore(2); //创建信号量，参数为该信号量，同时允许被获取的次数。
	}
	
	public void print(Object document){
		try {
			sh.acquire(2); //此方法可以被中断。另外acquireUninterruptibly()不允许中断，tryAcquire();tryAcquire(timeout, unit)这些方法与Lock是一样的意义。
			//System.out.println("sh.availablePermits() is "+sh.availablePermits());
			long duration = (long)(Math.random()*100);
			System.out.println(Thread.currentThread().getName()+ " Printed the document in "+ duration+"s");
            Thread.sleep(duration);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			sh.release(2);
			System.out.println("============="+sh.availablePermits());
		}
	}
}

class Job implements Runnable{

	private PrintQueue pq;
	
	public Job(PrintQueue pq){
		this.pq = pq;
	}
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+" started");
		pq.print(new Object());
		System.out.println(Thread.currentThread().getName()+" ended");
	}
	
}