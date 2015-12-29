package com.wang.concurrency.collection;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

//1.只有到达了激活条件的元素能被操作，否则不能。
//2.元素需要实现Delayed接口，该接口实现了Comparable接口
public class DelayedQueueTester {

	public static void main(String[] args) {
		DelayQueue<MyEvent> dq = new DelayQueue<MyEvent>();
		Thread[] adder = new Thread[5];
		Thread[] deleter = new Thread[5];
		for(int i=0;i<5;i++){
			adder[i] = new Thread(new MyTask(dq,i));
			adder[i].start();
		}
		for(int i=0;i<5;i++){
			try {
				adder[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("after add DelayQueue size is " + dq.size());
		for(int i=0;i<5;i++){
			deleter[i] = new Thread(new DeleteTask(dq));
			deleter[i].start();
			
		}
		for(int i=0;i<5;i++){
			try {
				deleter[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("after delete DelayQueue size is " + dq.size());
	}
}
//如果没有上面的延迟20秒的代码，则从结果来看，插入了50个元素，但是只能删除46个，有4个因为时间未到，无法删除
//after add DelayQueue size is 50
//can not poll element :1
//can not poll element :1
//can not poll element :1
//can not poll element :1
//after delete DelayQueue size is 4
//而如果有的话，因为在删除的时候，已经到了预计时间，所以时间的结果为如下。
//after add DelayQueue size is 50
//after delete DelayQueue size is 0

class MyTask implements Runnable{

	private DelayQueue<MyEvent> dq;
	private int id;
	
	public MyTask(DelayQueue<MyEvent>  dq, int id) {
		this.dq = dq;
		this.id = id;
	}

	@Override
	public void run() {
		for(int i=0;i<10;i++){
			//System.currentTimeMillis():毫秒数
			MyEvent e = new MyEvent(new Date(System.currentTimeMillis()+id*1000));
			dq.add(e);
		}
	}
}

class DeleteTask implements Runnable{

	private DelayQueue<MyEvent> dq;
	private int counter;
	
	public DeleteTask(DelayQueue<MyEvent>  dq) {
		this.dq = dq;
	}

	@Override
	public void run() {
		for(int i=0;i<10;i++){
			//返回第一个元素，如果没有数据，则返回null，非阻塞
			if(dq.poll()==null){
				//take，删除第一个元素，如果为空，则阻塞
				//peek，返回第一个元素，但是不删除。
				counter++;
				System.out.println("can not poll element :" + counter);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}



class MyEvent implements Delayed{

	private Date startDate;
	public MyEvent(Date startDate) {
		this.startDate = startDate;
	}

	//???跟谁比较？为啥要比较？
	//根据比较结果，排列元素的位置。比较是以纳秒级别比较的。
	
	@Override
	public int compareTo(Delayed o) {
		long diff = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
		if(diff>0){
			return 1;
		}else if(diff <0){
			return -1;
		}else{
			return 0;
		}
	}

	//作用是啥？根据当前时间和预计开始时间的比较结果来决定是否可以操作该元素
	//如果大于0，则说明未到预计时间，不能操作。小于0，可以操作。
	@Override
	public long getDelay(TimeUnit unit) {
		Date now = new Date();
		//getTime()返回的是毫秒数
		long diff = startDate.getTime() - now.getTime();
		return unit.convert(diff, TimeUnit.MICROSECONDS);
	}
	
}