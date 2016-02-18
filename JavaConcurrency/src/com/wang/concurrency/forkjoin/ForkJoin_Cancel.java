package com.wang.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoin_Cancel {

	public static void main(String[] args) {

		ForkJoinPool fjp = new ForkJoinPool();
		TaskManager tm = new TaskManager();
		int[] numbers = NumberGenerator.generate(1000);
		Searcher searcher = new Searcher(numbers, 0, 1000, 5, tm);
		fjp.execute(searcher);
		fjp.shutdown();
		try {
			fjp.awaitTermination(100, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main End");

	}

}

// 通过随机数产生整数数组
class NumberGenerator {

	public static int[] generate(int size) {
		int[] number = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			number[i] = random.nextInt(10);
			//System.out.println(number[i]);
		}
		return number;
	}
}

class TaskManager {

	private List<ForkJoinTask<Integer>> taskList;

	public TaskManager() {
		this.taskList = new ArrayList<ForkJoinTask<Integer>>();
	}

	public void addTask(ForkJoinTask<Integer> task) {
		taskList.add(task);
	}

	// 当在一个任务中找到后，就取消剩余的查找任务。
	public void cancelTasks(ForkJoinTask<Integer> cancelTask) {
		for (ForkJoinTask<Integer> task : taskList) {
			if (task != cancelTask) {
				if(task.cancel(true)){// 实际上这个true是无效的，来自api文档的说法，
									// 除非已创建未运行，运行或者结束的情况下是不能取消的。
				((Searcher)task).printCancelMessage();
				}
			}
		}
	}
}

class Searcher extends RecursiveTask<Integer> {

	private static final long serialVersionUID = -1479819212021482497L;

	private int start;

	private int end;

	private int[] numbers;

	private TaskManager tm;

	private int number;

	public Searcher(int[] numbers, int start, int end, int number, TaskManager tm) {
		this.start = start;
		this.end = end;
		this.numbers = numbers;
		this.tm = tm;
		this.number = number;
	}

	@Override
	protected Integer compute() {
		System.out.println("Task: " + start + " : " + end);
		int ret = 0;
		if (end - start < 10) {
			ret = lookforNumber();
			return ret;
		} else {
			int middle = (end + start) / 2;
			Searcher s1 = new Searcher(numbers, start, middle, number, tm);
			Searcher s2 = new Searcher(numbers, middle, end, number, tm);
			tm.addTask(s1);
			tm.addTask(s2);
			s1.fork();
			s2.fork();
			ret = s1.join();
			if (ret != -1) {
				return ret;
			}
			ret = s2.join();
			return ret;
		}
	}

	private int lookforNumber() {
		for (int i = start; i < end; i++) {
			if (number == numbers[i]) {
				System.out.println("Found the number at position " + i);
				tm.cancelTasks(this); // 取消剩余的task
				return i;
			}
		}
		return -1;
	}
	
	public void printCancelMessage(){
		System.out.printf("Task cancelled from %d to %d\n",start,end);
	}
}