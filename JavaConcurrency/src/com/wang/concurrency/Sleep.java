package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Sleep {
	class Clock implements Runnable{

		@Override
		public void run() {
			for(int i=0;i<10;i++){
				System.out.println(Thread.currentThread().getName()+"-" + i+" : "+ new Date());
				try{
					Thread.sleep(1000); //实际调用的是currentThread().sleep()方法
				}catch(InterruptedException e){
					System.out.println(Thread.currentThread().getName() + " is interrupted" );				}
			}
		}
	}
	
	public static void main(String[] args){
		Runnable clock = new Sleep().new Clock();
		Thread ct = new Thread(clock);
		ct.start();
		try {
			System.out.println("Thread in Main : "+Thread.currentThread().getName());
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + "is interrupted" );
		}
		ct.interrupt();
	}
}


