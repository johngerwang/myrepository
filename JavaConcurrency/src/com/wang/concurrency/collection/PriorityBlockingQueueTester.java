package com.wang.concurrency.collection;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTester {

	public static void main(String[] args) {
		PriorityBlockingQueue<Event> pbq = new PriorityBlockingQueue<Event>();
		Thread[] threads = new Thread[5];
		for(int i=0;i<threads.length;i++){
			threads[i] = new Thread(new Task(pbq));
			threads[i].start();
		}
		for(int i=0;i<threads.length;i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int i=0;i<threads.length*1000;i++){
			Event e = pbq.poll();
			System.out.println(e.getName()+" : "+ e.getPriority());
		}
		
		System.out.println("Main End");

	}

}

class Task implements Runnable{

	private PriorityBlockingQueue<Event> pbq;
	
	public Task(PriorityBlockingQueue<Event> pbq) {
		this.pbq = pbq;
	}

	@Override
	public void run() {
		for(int i=0;i<1000;i++){
			pbq.add(new Event(i,i));
		}
		
	}
	
}

class Event implements Comparable<Event>{

	private int name;
	public int getName() {
		return name;
	}


	public void setName(int name) {
		this.name = name;
	}


	public int getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}


	private int priority;
	
	
	public Event(int name, int priority) {
		this.name = name;
		this.priority = priority;
	}


	@Override
	public int compareTo(Event o) {
		if(this==o)		return 0;
		if(this.priority>o.priority){
			return 1;
		}else if(this.priority<o.priority){
			return -1;
		}else{
			return 0;
		}
	}
}


