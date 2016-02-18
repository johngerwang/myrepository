package com.wang.concurrency;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 面包房不停的生产面包，消费者不停的消费面包。
 * 有面包时通知消费者消费，否则消费者等待。面包满时，停止生产。
 * 没有面包时通知面包房生产面包，直到面包生产满，则停止生产。
 * 得使用synchronize来同步，使用wait&nofity来等待与唤醒。
 */
public class WaitAndNotify {

	public static void main(String[] args) throws InterruptedException {
		BreadWareHouse bwh = new BreadWareHouse();
		Thread p1 = new Thread(new Producer(bwh),"Producer1");
		Thread c = new Thread(new Consumer(bwh),"Consumer");
		Thread p2 = new Thread(new Producer(bwh),"Producer2");
		p1.start();
		c.start();
		p2.start();
		p1.join();
		c.join();
		p2.join();
	}

}

class BreadWareHouse{
	
	private int maxsize;
	private List<String> breads;
	private int getCounter;
	private int setCounter;
	
	public int getGetCounter() {
		return getCounter;
	}

	public void setGetCounter() {
		this.getCounter++;
	}
	public int getSetCounter() {
		return setCounter;
	}

	public void setSetCounter() {
		this.setCounter++;
	}

	public BreadWareHouse(){
		this.breads = new LinkedList<String>();
		this.maxsize = 10;
	}
	
	public synchronized String get(){
		while(breads.size()==0){//注意此处必须得是while，循环判断。如果是if，则只运行一次。
			try {
				wait(); //把当前线程加入到该对象的等待队列中
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String bread = ((LinkedList<String>)breads).poll();
		this.setGetCounter();
		System.out.println(Thread.currentThread().getName()+" Get the Bread: " + bread+" # "+ this.getGetCounter());
		notifyAll();
		//当不满时，会唤醒所有等待的线程，但是存在不能明确指定唤醒哪一个线程的问题，导致效率不高。
		//nofity()方法的话，则只能随机唤醒一个等待的线程让他进入竞争获取锁的状态，但是可能唤醒的是依然是消费者线程，同样效率不高。
		return  bread;
	}
	
	
	public synchronized void set(){
		while(breads.size()==maxsize){//注意此处必须得是while，循环判断。如果是if，则只运行一次。
			try {
				wait();//如果面包房满了，则不能继续放面包了，使该调用该对象的线程等待，并释放锁，使其他线程可以获得锁。后面的代码都不执行了。
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String bread = new Date().toString();
		breads.add(bread);
		this.setSetCounter();
		System.out.println(Thread.currentThread().getName()+ " Set the Bread: " + bread+" # "+this.getSetCounter());		
		notifyAll();
	}
}

class Consumer implements Runnable{

	private BreadWareHouse bwh;
	
	public Consumer(BreadWareHouse bwh){
		this.bwh = bwh;
	}
	@Override
	public void run() {
		for(int i=0;i<200;i++){
			bwh.get();
		}		
	}	
}

class Producer implements Runnable{

	private BreadWareHouse bwh;
	
	public Producer(BreadWareHouse bwh){
		this.bwh = bwh;
	}
	@Override
	public void run() {
		for(int i=0;i<100;i++){
			bwh.set();
		}		
	}
	
}