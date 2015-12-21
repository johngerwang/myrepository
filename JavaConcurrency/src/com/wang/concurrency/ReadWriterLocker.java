package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriterLocker {

	public static void main(String[] args) throws InterruptedException {

		PriceInfo pi = new PriceInfo();

		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new Reader(pi),"Reader");
			thread.start();
			//thread.join();
		}
//		for (int i = 0; i < 2; i++) {
			Thread thread = new Thread(new Writer(pi),"Writer");
			thread.start();
			//thread.join();
//		}

	}

}

class PriceInfo {

	private double price1 = 1;
	private double price2 = 2;

	private final static ReadWriteLock locker = new ReentrantReadWriteLock();

	public void setPrice(double price1, double price2) {
		locker.writeLock().lock();
		System.out.println(Thread.currentThread().getName()+ " Attemp to modify price");
		this.price1 = price1;
		this.price2 = price2;
		System.out.println(Thread.currentThread().getName() + " modified price1 " + price1 + " at: " + new Date());
		System.out.println(Thread.currentThread().getName() + " modified price2 " + price2 + " at: " + new Date());
		System.out.println(Thread.currentThread().getName()+ " finished modify price");
		locker.writeLock().unlock();
	}

	public void getPrice() {
		locker.readLock().lock();
		System.out.println(Thread.currentThread().getName()+ " Attemp to read price");
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
			pi.setPrice(10*(i+1), 20*(i+1));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}