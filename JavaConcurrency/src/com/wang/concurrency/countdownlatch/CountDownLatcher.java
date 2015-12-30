package com.wang.concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

//当CountDownLatch计数为0时，程序才能往下走，否则因为CountDownLatch#await方法，始终等待
//使用CountDownLatch时，需要创建一个CountDownLatch对象，构造函数也需要一个整数作为参数，
//可以把这个参数想象成一个倒计时器，CountDownLatch对象本身是一个发令枪，
//所有调用CountDownLatch.await方法的线程都会等待发令枪的指令，一旦倒计时器为0，
//这些线程同时开始执行，而CountDownLatch.countDown方法就是为倒计时器减1。

public class CountDownLatcher {

	public static void main(String[] args) throws InterruptedException {
		VideoConference vc = new VideoConference();
		Thread vct = new Thread(vc);
		vct.start();
		for(int i=0;i<10;i++){
			Participant pt = new Participant(vc);
			Thread ptt = new Thread(pt);
			Thread.sleep(1000);
			ptt.start();
		}
		

	}

}

class VideoConference implements Runnable{

	private final CountDownLatch latch;
	
	public VideoConference(){
		this.latch = new CountDownLatch(10);
	}
	
	public void arrived(Thread t){
		System.out.printf("%s is arrived\n",Thread.currentThread().getName());
		latch.countDown();
		System.out.printf("waiting for %d numbers\n", latch.getCount());
	}
	@Override
	public void run() {
		System.out.println("VideoConference initialization "+ latch.getCount() +" participants");
		try{
			latch.await(100, TimeUnit.SECONDS);
			System.out.println("VideoConference:All arrived,let's start");
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
}

class Participant implements Runnable{

	private VideoConference vc;
	
	public Participant(VideoConference vc){
		this.vc = vc;
	}
	
	@Override
	public void run() {
		vc.arrived(Thread.currentThread());
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}