package com.wang.concurrency;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Interrupted {
	public static void main(String[] args){
		Runnable fs = new FileSearcher(new File("."),".java");
		Thread task = new Thread(fs);
		task.start();
		try{
			TimeUnit.MILLISECONDS.sleep(10);
// 			TimeUnit.SECONDS.sleep(1);
//			TimeUnit.MINUTES.sleep(10);
//			Thread.sleep(10);
//			Thread.sleep(10*1000);
//			Thread.sleep(10*60*1000);
			//TimeUnit.MILLISECONDS.sleep(1);#只是对Thread.sleep()方法的包装，提供时间单位的换算。Thread.sleep()是毫秒级的，而此方法可以有秒级别的。
	//		Thread.sleep(1);
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		task.interrupt();
	}
}


class FileSearcher implements Runnable{

	private File initDir;
	private String searchFile;

	@Override
	public void run() {
		if(initDir.isDirectory()){
			try {
				processDir(initDir);
			} catch (InterruptedException e) {
				System.out.println("The Search was interrupted by "+Thread.currentThread().getName());
			}
		}else{
			System.out.println("Is not a directory: " + initDir.getName());
		}
		
	}

	private void processDir(File dir) throws InterruptedException{
		File[] files = dir.listFiles();
		for(int i =0;i<files.length;i++){
			if(files[i].isDirectory()){
				processDir(files[i]);
			}else{
				processFile(files[i]);
			}
		}
		if(Thread.interrupted()){ //其实就是调用的currentThread().isInterrupted(true);
			throw new InterruptedException(); 
		}
	}
	
	private void processFile(File file)throws InterruptedException{
		if(file.getName().contains(searchFile)){
			System.out.println(Thread.currentThread().getName()+" found the file " + file.getName());
		}
		if(Thread.interrupted()){
			throw new InterruptedException();
		}
	}
	
	public FileSearcher(File initDir, String searchFile) {
		super();
		this.initDir = initDir;
		this.searchFile = searchFile;
	}
	
}