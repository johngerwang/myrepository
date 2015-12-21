package com.wang.concurrency;

import java.util.concurrent.*;

class Sleeper extends Thread {
	// private String name;
	private int duration;

	public Sleeper(String name, int duration) {
		super(name);
		this.duration = duration;
		start();
	}

	public void run() {
		try {
			System.out.println(getName() + " before sleep.");
			TimeUnit.MILLISECONDS.sleep(duration);
			System.out.println(getName() + " is asleep.");
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted. " + " isInterrupted(): " + isInterrupted());
			return;
		}
		System.out.println(getName() + " has awakened.");
	}
}

class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}

	public void run() {
		try {
			System.out.println(getName() + " before join.");
			sleeper.join();
			System.out.println(getName() + " after join.");
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted.");
		}
		System.out.println(getName() + " join completed.");
	}
}

public class InterruptAndJoin {
	public static void main(String args[]) {
		//Sleeper sleepy = new Sleeper("Sleepy", 3000);
		Sleeper grumpy = new Sleeper("Sleeper", 3000);
		//Joiner dopey = new Joiner("Dopey", sleepy);
		Joiner doc = new Joiner("Joiner", grumpy);

		grumpy.interrupt();//把子线程打断后，就可以让父线程继续执行了，否则等待。

	}
}