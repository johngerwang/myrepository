package com.wang.concurrency.lock;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockMonitor {

	public static void main(String[] args) {
		MyLock locker = new MyLock();
		Thread[] threads = new Thread[5];
		for(int i=0;i<5;i++){
			threads[i] = new Thread(new MyTask(locker));
			threads[i].start();
		}
		for(int i=0;i<15;i++){
			System.out.println("===========Logging the Lock info==========");
			System.out.println("Lock Owner: " + locker.getOwnerName());
			System.out.println("Lock has the queued threads: "+ locker.hasQueuedThreads());
			if(locker.hasQueuedThreads()){
				System.out.println(locker.getQueueLength());
				Collection<Thread> ct = locker.getThreads();
				for(Thread t:ct){
					System.out.println(t.getName());
				}
			}
			System.out.println("Fairness: "+ locker.isFair());
			System.out.println("Lock is locked: " + locker.isLocked());
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}

class MyLock extends ReentrantLock {

	private static final long serialVersionUID = -6340784184572549794L;

	public String getOwnerName(){
		return (this.getOwner()==null)?"None":this.getOwner().getName();
	}
	
	//返回正在等待获取锁的线程集合
	public Collection<Thread> getThreads(){
		return this.getQueuedThreads();
	}
}

class MyTask implements Runnable{

	public MyTask(Lock lock) {
		super();
		this.lock = lock;
	}
	private Lock lock;
	@Override
	public void run() {
		lock.lock();
		System.out.println("Get the lock: " + Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
			System.out.println("Free the lock: " + Thread.currentThread().getName());

		}
	}
	
}
