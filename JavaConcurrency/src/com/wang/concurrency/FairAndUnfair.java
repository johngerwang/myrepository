package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfair {

	public static void main(String[] args) throws InterruptedException {

		NewPrinter pter = new NewPrinter();

		for (int i = 0; i < 5; i++) {
			Thread thread0 = new Thread(new NewPrintTask(pter), "P"+i);
			Thread.sleep(100);
			thread0.start();
			thread0.join();
			//此处如果加了join方法，则会让每一个线程连续获取2次锁(看print()行数，有2次获取锁)，因为线程是一个一个启动的，
			//如果子线程(join到主线程(main)的线程没有执行完，主线程就不会生成&启动下一个线程。
			//而如果不加join，则所有线程都顺序生产/启动，没有等等某一个线程执行完再去生产/启动下一个线程的问题。
		}
	}

}

class NewPrinter {

	// 锁一旦被释放，使等等事件最长的线程开始执行。所谓的公平锁。
	//默认无参数的构造函数是非公平锁，实际的参数是false。
	private Lock printerLocker = new ReentrantLock();

	public void print() throws InterruptedException {
		printerLocker.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " Start printing " + new Date());
			// TimeUnit.SECONDS.sleep(3);
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + " End printing " + new Date());
		} catch (InterruptedException e) {
			System.out.println("print() method is interrupted");
		} finally {
			printerLocker.unlock(); // 记得此处一定要释放锁，不然其他线程无法再次获得了。
		}
		printerLocker.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " Start printing" + new Date());
			// TimeUnit.SECONDS.sleep(3);
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + " End printing " + new Date());
		} catch (InterruptedException e) {
			System.out.println("print() method is interrupted");
		} finally {
			printerLocker.unlock(); // 记得此处一定要释放锁，不然其他线程无法再次获得了。
		}
	}
}

class NewPrintTask implements Runnable {

	private NewPrinter pt;

	public NewPrintTask(NewPrinter pr) {
		this.pt = pr;
	}

	@Override
	public void run() {
		// System.out.println(Thread.currentThread().getName() + " started");
		try {
			pt.print();
		} catch (InterruptedException e1) {
			System.out.println(Thread.currentThread().getName() + " Interrrupted from runJob.");
		}
		// System.out.println(Thread.currentThread().getName() + " ended");
	}

}