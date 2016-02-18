package com.wang.concurrency;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * 守护线程，优先级底，一般用来给用户线程如Main，自定义的线程服务的。当程序中没有其他线程运行时才运行。
 * 可以看一下输出结果，守护线程运行的次数较少--》也就是说优先级低了。
 */
public class Deamon {
	public static void main(String[] args) {
		Deque<Deamon.Event> dq = new ArrayDeque<Deamon.Event>();
		for (int i = 0; i < 1; i++) {
			Thread wt = new Thread(new Deamon().new WriteTask(dq));// 注意写法
			wt.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Thread ct = new Thread(new Deamon().new Cleaner(dq));
		ct.setDaemon(true);
		ct.start();
	}

	class Event {
		@Override
		public String toString() {
			return "Event [dt=" + dt + ", name=" + name + "]";
		}

		public Date getDt() {
			return dt;
		}

		public void setDt(Date dt) {
			this.dt = dt;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private Date dt;
		private String name;
	}

	class WriteTask implements Runnable {
		private Deque<Deamon.Event> dq;// 注意写法

		public WriteTask(Deque<Deamon.Event> dq) {
			this.dq = dq;
		}

		@Override
		public void run() {
			for (int i = 0; i < 20; i++) {
				Deamon.Event de = new Deamon.Event();
				de.setDt(new Date());
				de.setName(Thread.currentThread() + "#" + i);
				dq.addFirst(de);
				System.out.println("add the event:" + de);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class Cleaner implements Runnable {

		private Deque<Deamon.Event> dq;

		public Cleaner(Deque<Deamon.Event> dq) {
			this.dq = dq;
		}

		@Override
		public void run() {
			if (dq.size() == 0) {
				return;
			}
			while (true) {
			//	System.out.println("Deamon thread is running");
				if (dq.size() > 0) {
					Event e = dq.getLast();
					long diff = new Date().getTime() - e.getDt().getTime();
					if (diff > 100) {
						dq.removeLast();
						System.out.println(new Date() + " ： Removed the event: " + e);
						System.out.println(new Date() + " : Current Deque size: " + dq.size());
					}
				}
			}
		}
	}

}
