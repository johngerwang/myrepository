package com.wang.concurrency.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

//注意点&所犯错误
//1.一定不能多/少调用arriveAndAwaitAdvance，否则就跟注册的任务不同了，那么就错误了
//2.switch一定要break，或者return
//3.如果onAdvance返回true，则说明phaser已经终止，那么arriveAndAwaitAdvance也就不起作用了。
public class PhaserTester2 {

	public static void main(String[] args) {
		Phaser myphaser = new MyPhaser();
		Thread[] t = new Thread[3];
		Student[] st = new Student[3];
		for (int i = 0; i < 3; i++) {
			st[i] = new Student(myphaser);
			myphaser.register();
		}
		for (int i = 0; i < 3; i++) {
			t[i] = new Thread(st[i]);
			t[i].start();
		}
		for (int i = 0; i < 3; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Main the phaser is finished " + myphaser.isTerminated());

	}

}

class MyPhaser extends Phaser {

	// 在所有在因arriveAndAwaitAdvance()方法而休眠的线程唤醒之前（可以理解为准备唤醒了），此方法被回调，然后就被唤醒。
	@Override
	protected boolean onAdvance(int phase, int parties) {
		System.out.println("registeredParties: " + parties);
		switch (phase) {
		case 0:
			return studentArrived();  //注意，必须return（需要返回值时），否则会一直往下执行
		case 1:
			return finishedFirstExercise();
		case 2:
			return finishedSecondExercise();
		case 3:
			return finishedExam();
		default:
			return true;
		}
	}

	private boolean studentArrived() {
		System.out.println("we have " + this.getRegisteredParties() + " students arrived.Current phase is " +this.getPhase());
		return false;
	}

	private boolean finishedFirstExercise() {
		System.out.println("All Students finished the first exercise,it's time to go to second.Current phase is " +this.getPhase());
		return false;
	}

	private boolean finishedSecondExercise() {
		System.out.println("All Students finished the second exercise,it's time to go to third .Current phase is " +this.getPhase());
		return false;
	}

	private boolean finishedExam() {
		System.out.println("All Students finished the all exercise .Current phase is " +this.getPhase());
		return true;
	}
}

class Student implements Runnable {

	private Phaser phaser;

	public Student(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		this.studentArrived();
		this.doFirstExercise();
		this.doSecondExercise();
		this.doThirdExercise();

	}

	private void studentArrived() {
		System.out.println(Thread.currentThread().getName() + " is arrived");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("studentArrived() is " + phaser.arriveAndAwaitAdvance());

		//phaser.arriveAndAwaitAdvance();
	}

	private void doFirstExercise() {
		System.out.println(Thread.currentThread().getName() + " is going to do first exercise");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " has done the first exercise");
		System.out.println("doFirstExercise1() is " + phaser.arriveAndAwaitAdvance());
	}

	private void doSecondExercise() {
		System.out.println(Thread.currentThread().getName() + " is going to do second exercise");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " has done the second exercise");
		System.out.println("doSecondExercise() is " + phaser.arriveAndAwaitAdvance());
		//phaser.arriveAndAwaitAdvance();
	}

	private void doThirdExercise() {
		System.out.println(Thread.currentThread().getName() + " is going to do third exercise");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " has done the third exercise");		
		System.out.println("doThirdExercise() is " + phaser.arriveAndAwaitAdvance());
		//phaser.arriveAndAwaitAdvance();

	}
}