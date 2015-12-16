package com.wang.concurrency;

import java.io.FileWriter;
import java.io.PrintWriter;

public class MultiThread {

	private static int number =4;
	public static void main(String[] args) {
		PrintWriter pw =null;
		Thread[] threads = new Thread[number];
		Thread.State states[] = new Thread.State[number];
		try {
			FileWriter fw = new FileWriter("threads_log.txt");
			pw = new PrintWriter(fw);
			for (int i = 0; i < number; i++) {
				threads[i] = new Thread(new Caculator(i));
				threads[i].setName("Thread" + i);
				states[i] = threads[i].getState();
				if (i % 2 == 0) {
					threads[i].setPriority(Thread.MAX_PRIORITY);
				} else {
					threads[i].setPriority(Thread.MIN_PRIORITY);
				}
				pw.println("Main.Thread " + threads[i].getName() + "'s Status is " + states[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < number; i++) {
			threads[i].start();
		}

		boolean finished = false;
		while (!finished) {
			for (int i = 0; i < number; i++) {
				if (states[i] != threads[i].getState()) {
					writeThreadsInfo(pw, threads[i], states[i]);
					states[i] = threads[i].getState();
				}
			}
		finished = true;
		for (int i = 0; i < number; i++) {
			finished = finished && (threads[i].getState() == Thread.State.TERMINATED);
		}
		}
		if (finished) {
			pw.close();
		}

	}

	public static void writeThreadsInfo(PrintWriter pw, Thread thread, Thread.State threadStatus) {
		pw.println("=========================");
		pw.println("Main.Thread Name is " + thread.getName());
		pw.println("Main.Thread ID is " + thread.getId());
		pw.println("Main.Thread Priority is " + thread.getPriority());
		pw.println("Main.Old State is " + threadStatus);
		pw.println("Main.New State is " + thread.getState());
		pw.println("=========================");

	}
}

class Caculator implements Runnable {

	private int number = 0;

	public Caculator(int number) {
		this.number = number;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.printf("%s:%d*%d=%d\n", Thread.currentThread().getName(), number, i, i * number);

		}
	}

}
