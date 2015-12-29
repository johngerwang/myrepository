package com.wang.concurrency.executor.schedule.threadpoolexecutor;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RunnableFutureTaskTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

class MySchduledTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {

	private RunnableScheduledFuture<V> task;

	private ScheduledThreadPoolExecutor executor;

	private long period;

	private long startDate;

	public MySchduledTask(Runnable runnable, V result, RunnableScheduledFuture<V> task,
			ScheduledThreadPoolExecutor executor) {
		super(runnable, result);
		this.executor = executor;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		if (!this.isPeriodic()) {// 非周期任务
			return this.getDelay(unit);
		} else {
			if (startDate == 0) {// 周期任务，但是开始时间为0
				return this.getDelay(unit);
			}
			long delay = startDate - new Date().getTime();
			return unit.convert(delay, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public int compareTo(Delayed o) {
		return task.compareTo(o);

	}

	@Override
	public boolean isPeriodic() {
		return task.isPeriodic();
	}

	@Override
	public void run() {
		if (this.isPeriodic() && (!executor.isShutdown())) {
			Date now = new Date();
			startDate = now.getTime() + period;
			executor.getQueue().add(this);
			System.out.println("Pre-Current Time: " + new Date());
			System.out.println("Is Periodic: " + this.isPeriodic());
			super.runAndReset();// ?干嘛用
			System.out.println("Post-Current Time: " + new Date());

		}
	}

	public void setPeriod(long period) {
		this.period = period;
	}
}

class MyScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	public MyScheduledThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
		MySchduledTask<V> mytask = new MySchduledTask<V>(runnable, null, task, this);
		return mytask;
	}
}
