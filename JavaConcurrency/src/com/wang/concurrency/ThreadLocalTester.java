package com.wang.concurrency;

import java.util.Date;

//ThreadLocal，每一个线程单独一个值，不共享。值是通过set和get方法来操作的。每一个线程开始时，都会通过initValue初始化一个值
public class ThreadLocalTester {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			Thread safe = new Thread(new Safe());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			safe.start();
		}

		// SequenceNumber sn = new SequenceNumber();
		// TestClient t1 = new TestClient(sn);
		// TestClient t2 = new TestClient(sn);
		// TestClient t3 = new TestClient(sn);
		//
		// t1.start();
		// t2.start();
		// t3.start();
		//
		// t1.print();
		// t2.print();
		// t3.print();
		//
	}
}

class Safe implements Runnable {

	//此处推荐使用static final，因为ThreadLocal对象是不会改变的，而initialValue是每一个对象会调用一次。
	private static ThreadLocal<Date> td = new ThreadLocal<Date>() {
		protected Date initialValue() {
			Date d = new Date();
			System.out.println(td.getClass().getDeclaredMethods() + " " + d);
			return d;
		}
	};

	@Override
	public void run() {
		System.out.println("before: " + Thread.currentThread().getName() + " : " + td.get());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		td.set(new Date());
		System.out.println("after: " + Thread.currentThread().getName() + " : " + td.get());
	}
}

class SequenceNumber {

	private ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
		public Integer initialValue() {
			return 0;
		}
	};

	public int getNextNum() {
		seqNum.set(seqNum.get() + 1);
		return seqNum.get();
	}
}

class TestClient extends Thread {
	private SequenceNumber sn;

	public TestClient(SequenceNumber sn) {
		this.sn = sn;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println(Thread.currentThread().getName() + " --> " + sn.getNextNum());
		}
	}

	public void print() {
		for (int i = 0; i < 3; i++) {
			System.out.println(Thread.currentThread().getName() + " --> " + sn.getNextNum());
		}
	}
}
