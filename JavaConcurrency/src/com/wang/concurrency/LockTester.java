package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTester {

	public static void main(String[] args) throws InterruptedException {

			Printer pter = new Printer();
			Thread thread = new Thread(new PrintTask(pter));
			thread.start();
			//thread.join();
			//为什么此处有打断操作，但是不会被执行。而如果去掉了上面的join则会执行。
			//原因是，因为main线程因为thread.join()的调用而阻塞掉了，下面的thread.interrupt()无法调用，
			//当thread执行完后，即使再调用interrupt也无济于事了。
			thread.interrupt();
			System.out.println("Main Thread over");
	}

}

class Printer {

	private final static Lock printerLocker = new ReentrantLock();

	public void print() throws InterruptedException {
		//printerLocker.lockInterruptibly(); //不会去尝试获得锁，有中断立即抛出InterruptedException
		printerLocker.lock();//获得锁后才中断
		try {
			System.out.println(
					Thread.currentThread().getName() + " : after got the lock,interrupte flag is: " + Thread.currentThread().isInterrupted());
			System.out.println("Start printing " + new Date());
//			TimeUnit.SECONDS.sleep(3);
			Thread.sleep(3000);
			System.out.println("End printing " + new Date());
		} catch (InterruptedException e) {
			System.out.println(
					Thread.currentThread().getName() + " :in catch,interrupte flag is: " + Thread.currentThread().isInterrupted());
		} finally {
			//一旦抛出中断异常后，中断标志就被清除了，所以下面就中断标志为false
			System.out.println(
					Thread.currentThread().getName() + " :after catch,interrupte flag is: " + Thread.currentThread().isInterrupted());
			printerLocker.unlock(); // 记得此处一定要释放锁，不然其他线程无法再次获得了。
		}
	}
}

class PrintTask implements Runnable {

	private Printer pt;

	public PrintTask(Printer pr) {
		this.pt = pr;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " started");
		try {
			pt.print();
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			System.out.println(Thread.currentThread().getName() + " Interrrupted from run.");
		}
		System.out.println(Thread.currentThread().getName() + " ended");
	}

}