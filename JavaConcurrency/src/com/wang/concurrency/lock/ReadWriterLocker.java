package com.wang.concurrency.lock;

import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 说到ReentrantReadWriteLock，首先要做的是与ReentrantLock划清界限。它和后者都是单独的实现，彼此之间没有继承或实现的关系
 * ReadWriteLock 描述的是：一个资源能够被多个读线程访问，或者被一个写线程访问，但是不能同时存在读写线程。
 * 也就是说读写锁使用的场合是一个共享资源被大量读取操作，而只有少量的写操作（修改数据）
 * 
 * 读写锁：分为读锁和写锁，多个读锁不互斥，读锁与写锁互斥，这是由jvm自己控制的，
 * 你只要上好相应的锁即可。如果你的代码只读数据，可以很多人同时读，但不能同时写，
 * 那就上读锁；如果你的代码修改数据，只能有一个人在写，且不能同时读取，那就上写锁。 总之，读的时候上读锁，写的时候上写锁！ 
 * 
 * 线程进入读锁的前提条件：1.没有其他线程的写锁. 2.没有写锁。
 * ->有读的时候，可以再读。有写的时候不能读。
 * 线程进入写锁的前提条件：1.没有其他线程的读锁  2.没有其他线程的写锁
 * ->有读或者写的时候，都不能写。
 * 
 * 锁降级 写线程获取写入锁后可以获取读取锁，然后释放写入锁，这样就从写入锁变成了读取锁，从而实现锁降级的特性。 
 * 锁升级 读取锁是不能直接升级为写入锁的。因为获取一个写入锁需要释放所有读取锁，所以如果有两个读取锁视图获取写入锁而都不释放读取锁时就会发生死锁。
 * 
 * 
 * 如何理解可重入
 * 重入性
   读写锁允许读线程和写线程按照请求锁的顺序重新获取读取锁或者写入锁。当然了只有写线程释放了锁，读线程才能获取重入锁。
   写线程获取写入锁后可以再次获取读取锁，但是读线程获取读取锁后却不能获取写入锁。
   另外读写锁最多支持65535个递归写入锁和65535个递归读取锁。
   
 * 重入数
   读取锁和写入锁的数量最大分别只能是65535（包括重入数）
   
   写入锁提供了条件变量(Condition)的支持，这个和独占锁一致，但是读取锁却不允许获取条件变量，将得到一个UnsupportedOperationException异常。
 */
public class ReadWriterLocker {

	public static void main(String[] args) throws InterruptedException {

		PriceInfo pi = new PriceInfo();

		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Reader(pi), "Reader");
			thread.start();
			// thread.join();
		}
		// for (int i = 0; i < 2; i++) {
		Thread thread = new Thread(new Writer(pi), "Writer");
		thread.start();
		// thread.join();
		// }

	}

}

class PriceInfo {

	private double price1 = 1;
	private double price2 = 2;

	private final static ReadWriteLock locker = new ReentrantReadWriteLock();

	public void setPrice(double price1, double price2) {
		locker.writeLock().lock();
		System.out.println(Thread.currentThread().getName() + " Attemp to modify price");
		this.price1 = price1;
		this.price2 = price2;
		System.out.println(Thread.currentThread().getName() + " modified price1 " + price1 + " at: " + new Date());
		System.out.println(Thread.currentThread().getName() + " modified price2 " + price2 + " at: " + new Date());
		System.out.println(Thread.currentThread().getName() + " finished modify price");
		locker.writeLock().unlock();
	}

	public void getPrice() {
		locker.readLock().lock();
		System.out.println(Thread.currentThread().getName() + " Attemp to read price");
		System.out.println(Thread.currentThread().getName() + " read price1 " + price1 + "  at: " + new Date());
		System.out.println(Thread.currentThread().getName() + " read price2 " + price2 + "  at: " + new Date());
		locker.readLock().unlock();
	}
}

class Reader implements Runnable {

	private PriceInfo pi;

	public Reader(PriceInfo pf) {
		this.pi = pf;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			pi.getPrice();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Writer implements Runnable {

	private PriceInfo pi;

	public Writer(PriceInfo pf) {
		this.pi = pf;
	}

	@Override
	public void run() {
		for (int i = 0; i < 3; i++) {
			pi.setPrice(10 * (i + 1), 20 * (i + 1));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}