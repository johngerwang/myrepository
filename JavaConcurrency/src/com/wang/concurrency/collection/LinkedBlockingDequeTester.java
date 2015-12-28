package com.wang.concurrency.collection;

import java.util.concurrent.LinkedBlockingDeque;

//同步的双向链表。最大元素为100
//当LinkedBlockingDeque无数据时，但是要取的时候，会阻塞take()方法。当满的时候，要插入，也阻塞put方法。

public class LinkedBlockingDequeTester {

	public static void main(String[] args) {
		LinkedBlockingDeque<String> cd  = new LinkedBlockingDeque<String>(100);
		Thread[] threads = new Thread[5];
		for(int i=0;i<threads.length;i++){
			threads[i] = new Thread(new AdderTask(cd));
			threads[i].start();
		}
//		for(int i=0;i<threads.length;i++){
//			try {
//				threads[i].join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		System.out.println("Main: after add list size is " + cd.size());
		for(int i=0;i<threads.length;i++){
			threads[i] = new Thread(new RemoverTask(cd));
			threads[i].start();
		}
		for(int i=0;i<threads.length;i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Main: after remove list size is " + cd.size());
	}

}

class AdderTask implements Runnable{

	private LinkedBlockingDeque<String> cd ;
	
	public AdderTask(LinkedBlockingDeque<String> cd){
		this.cd = cd;
	}
	
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		for(int i=0;i<20;i++){
			try {
				cd.put(name+"#"+i);//同步方法
				//addFirst,addLast(),当满时，抛出IllegalStateException
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class RemoverTask implements Runnable{

	private LinkedBlockingDeque<String> cd ;
	
	public RemoverTask(LinkedBlockingDeque<String> cd){
		this.cd = cd;
	}
	
	@Override
	public void run() {
		for(int i=0;i<20;i++){
			try {
				cd.take();//同步方法
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//takefirst,takelast，阻塞
			//getFirst(),getLast()，元素不会被移除，但是如果遇到空的list则抛出NoSuchElementException
			//peekFirst(),peekLast()，不会移除，但是如果遇到空的list则返回null
			//pollFirst()，pollFirst()。会移除，但是如果遇到空的list则返回null
		}
	}
}