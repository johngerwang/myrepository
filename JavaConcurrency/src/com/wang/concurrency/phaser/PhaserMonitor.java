package com.wang.concurrency.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
/**
 * 
 *Phaser同时包含CyclicBarrier和CountDownLatch两个类的功能。
 *Phaser的arrive方法将将计数器加1，awaitAdvance将线程阻塞，直到计数器达到目标，
 *这两个方法与CountDownLatch的countDown和await方法相对应；
 *Phaser的arriveAndAwaitAdvance方法将计数器加1的同时将线程阻塞，
 *直到计数器达到目标后继续执行，这个方法对应CyclicBarrier的await方法。 
 * 
 */
public class PhaserMonitor {

	public static void main(String[] args) {
		Phaser phaser = new Phaser(3);
		Thread[] threads = new Thread[3];
		for(int i=0;i<3;i++){
			MyPhaser1 my = new MyPhaser1(3,phaser);
			threads[i] = new Thread(my);
			threads[i].start();
		}
		for(int i=0;i<10;i++){
			System.out.println("=======Main: Phaser log start========");
			System.out.println("Main: Phase: "+ phaser.getPhase());
			System.out.println("Main: registered Parties: " +phaser.getRegisteredParties());
			System.out.println("Main: arrived parties: "  + phaser.getArrivedParties());
			System.out.println("Main: unarrived parties: "  + phaser.getUnarrivedParties());
			System.out.println("=======Main: Phaser log end========");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}

class MyPhaser1 implements Runnable{

	private int delayTime;
	private Phaser phaser;
	
	public MyPhaser1(int delayTime, Phaser phaser) {
		super();
		this.delayTime = delayTime;
		this.phaser = phaser;
	}

	@Override
	public void run() {
		phaser.arrive();
		//System.out.println("MyPhaser1: "+Thread.currentThread().getName()+" : "+ phaser.getPhase());
		System.out.println("Phase 1 started: "+ Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Phase 1 end: "+ Thread.currentThread().getName());
		phaser.arriveAndAwaitAdvance();
		
		System.out.println("Phase 2 started: "+ Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Phase 2 end: "+ Thread.currentThread().getName());
		phaser.arriveAndAwaitAdvance();
		
		System.out.println("Phase 3 started: "+ Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Phase 3 end: "+ Thread.currentThread().getName());
		phaser.arriveAndDeregister();
	}
	
}