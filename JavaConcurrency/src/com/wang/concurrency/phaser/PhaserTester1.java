package com.wang.concurrency.phaser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * register()  新注册一个线程到Phaser中，该线程被认为未完成本阶段的任务
 * bulkRegister(int count) 和register一样，只是指定个数。
 * forceTermination() 强迫终止Phaser
 * 
*/
public class PhaserTester1 {

	public static void main(String[] args) {
		String suffix = ".java";
		//被Phaser置于休眠状态的线程不会响应中断，更不会抛出InterruptedExpceiton
		Phaser pr = new Phaser(3);//参数为线程数
		System.out.println(pr.getRegisteredParties());
		FileSearcher fs1 = new FileSearcher(new File("/Users/johnwang/gitrep/myrepository/JavaConcurrency/src/com/wang/concurrency"),suffix,pr);
		FileSearcher fs2 = new FileSearcher(new File("/Users/johnwang/gitrep/myrepository/JavaConcurrency/src/com/wang/concurrency/countdownlatch"),suffix,pr);
		FileSearcher fs3 = new FileSearcher(new File("/Users/johnwang/gitrep/myrepository/JavaConcurrency/src/com/wang/concurrency/cyclicbarrier"),suffix,pr);
		Thread t1 = new Thread(fs1);
		Thread t2 = new Thread(fs2);
		Thread t3 = new Thread(fs3);
		t1.start();
		t2.start();
		t3.start();
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(pr.isTerminated());
	}

}

class FileSearcher implements Runnable {

	private File initPath;
	private String suffix;
	private List<String> results;
	private Phaser phaser;

	public FileSearcher(File initPath, String suffix,Phaser phaser) {
		this.initPath = initPath;
		this.suffix = suffix;
		this.results = new ArrayList<String>();
		this.phaser = phaser;
	}

	//STEP1.1，获得所有的以suffix结尾的文件的路径
	public void direcotryProcess(File initPath) {
		File[] files = initPath.listFiles();
		if (null != files) { //非目录时返回null
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					direcotryProcess(files[i]);
				} else {
					fileProcess(files[i]);
				}
			}
		}
		
	}
	
	//STEP1.2 获得所有的以suffix结尾的文件的路径
	public void fileProcess(File file) {
		if (file.getName().endsWith(suffix)) {
			results.add(file.getAbsolutePath());
		}

	}
	
	//STEP1.3 &STEP2.2.检查是未获得任何suffix结尾的路径，是，则结束该线程。
	public boolean checkEmpty(){
		if(results.isEmpty()){
			System.out.println(Thread.currentThread().getName() + " : " +"Phase: "+phaser.getPhase()+ ". Results: 0");
			phaser.arriveAndDeregister(); //删除该线程，不会因为该线程的未完成后不进行后续操作
			return true;
		}
		System.out.println(Thread.currentThread().getName() + " : " +"Phase: "+phaser.getPhase()+ ". Results: " + results.size());
		//该方法在phaser为终止状态时，会返回一个负数。如果可能会被终止状态，则应该要判断返回值，根据返回值来处理。	
		//waitAdvance(int phase),当给定的phase与当前的phase一致时，进入休眠，直到本阶段所有线程执行完毕。否则，立即返回
		
		//arrive()，不等待其他线程，继续往下执行。
		phaser.arriveAndAwaitAdvance(); //本例子中，phaser遇到3次该方法的调用后，才会继续执行后续动作。所以如果完成一个阶段后，每一个线程都得调用一次该方法。
		return false;
	}

	//STEP2.1 获得所有在24小时以内，且以suffix结尾的路径
	public void filterFileIn24H(){
		List<String> files = new ArrayList<String>();
		results.forEach(file->{if(new Date().getTime()-new File(file).lastModified()>TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)){ files.add(file);}});
		results = files;
	}
	
	public void showInfo(){
		results.forEach(file->System.out.println(Thread.currentThread().getName()+": "+ file));
		phaser.arriveAndAwaitAdvance();
	}
	
	@Override
	public void run() {
		phaser.arriveAndAwaitAdvance(); //等待所有线程都启动后开始查找
		
		this.direcotryProcess(initPath);
		System.out.println("after search before filter");
		results.forEach(file->System.out.println(Thread.currentThread().getName()+": "+ file));

		if(this.checkEmpty()){
			return;
		}

		this.filterFileIn24H();
		
		
		if(this.checkEmpty()){
			return;
		}
		
		this.showInfo();
		phaser.arriveAndDeregister();
		
		System.out.println("Completed");
	}

}