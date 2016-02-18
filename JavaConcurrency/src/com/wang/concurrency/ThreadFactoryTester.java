package com.wang.concurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 *使用ThreadFactory工厂模式生产线程。好处如，可以控制线程的数量 。
 * 必须要实现ThreadFactory接口的newThread(Runnable r)方法
 */
public class ThreadFactoryTester {

	public static void main(String[] args) {
		MyThreadFactory mtf = new MyThreadFactory("mythreadpool");
		for (int i = 0; i < 20; i++) {
			Thread t = mtf.newThread(new Task());
			if (t != null)
				t.start();
		}
		System.out.println(mtf.getStatus());
		System.out.println("Total is "+ mtf.getCounter());
	}
}
	class MyThreadFactory implements ThreadFactory {

		private int counter;
		private String name;
		private List<String> status;

		public MyThreadFactory(String name) {
			this.name = name;
			counter = 0;
			status = new ArrayList<String>();

		}

		public int getCounter(){
			return counter;
		}
		//本方法只是为了将所有的线程信息封装到StringBuffer中，以便输出而已。
		public String getStatus(){
			StringBuffer sb = new StringBuffer();
			Iterator<String> it = status.iterator();
			while(it.hasNext()){
				sb.append(it.next());
				sb.append("\n");
			}
			return sb.toString();
		}
		@Override
		public Thread newThread(Runnable r) {
			if (counter < 10) {
				Thread thread = new Thread(r, name + "_Thread_" + counter);
				counter++;
				//将线程信息加入到list中。在getStatus方法中拼装成字符串
				status.add(String.format("Thread name is %s,id is %d,date is %s\n", thread.getName(),
						thread.getId(), new Date()));
				return thread;
			}
			return null;
		}

	}

class Task implements Runnable{

	@Override
	public void run() {

		try {
			System.out.println(Thread.currentThread().getName()+" "+Thread.currentThread().getId());
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}