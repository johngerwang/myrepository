package com.wang.concurrency.collection;

import java.util.concurrent.ConcurrentLinkedDeque;

//非阻塞同步的双向链表。如果删除时，遇到没有元素，则返回null	
public class ConcurrentLinkedDequeTester {

	public static void main(String[] args) {
		ConcurrentLinkedDeque<String> cd  = new ConcurrentLinkedDeque<String>();
		Thread[] threads = new Thread[100];
		for(int i=0;i<threads.length;i++){
			threads[i] = new Thread(new AddTask(cd));
			threads[i].start();
		}
		for(int i=0;i<threads.length;i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Main: after add list size is " + cd.size());
		for(int i=0;i<threads.length;i++){
			threads[i] = new Thread(new RemoveTask(cd));
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

class AddTask implements Runnable{

	private ConcurrentLinkedDeque<String> cd ;
	
	public AddTask(ConcurrentLinkedDeque<String> cd){
		this.cd = cd;
	}
	
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		for(int i=0;i<10000;i++){
			cd.add(name+"#"+i);
		}
	}
}

class RemoveTask implements Runnable{

	private ConcurrentLinkedDeque<String> cd ;
	
	public RemoveTask(ConcurrentLinkedDeque<String> cd){
		this.cd = cd;
	}
	
	@Override
	public void run() {
		for(int i=0;i<5000;i++){
			cd.pollFirst();
			cd.pollLast();
		}
	}
}