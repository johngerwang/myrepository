package com.wang.concurrency.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

//fork+join的搭配，其实就是invoke()一个方法执行的动作。
//功能概要：Finder是一个RecursiveTask，返回以extension结尾的文件的个数。如果是一个目录，则新建一个Finder任务，使用fork异步方法发送到线程池
//使用join方法获得返回值。

public class ForkJoin_Asychronous {

	public static void main(String[] args) {
		ForkJoinPool fjp = new ForkJoinPool();
		String extension ="java";
		String path = "/Users/johnwang/gitrep/myrepository/JavaConcurrency/src/com/wang/concurrency/";
		Finder finder = new Finder(path,extension);
		fjp.submit(finder);
		//fjp.execute(finder);
		do{
			System.out.println("Main ForkJoinPool.getParallelism(): "+fjp.getParallelism());
			System.out.println("Main ForkJoinPool.getActiveThreadCount(): " +fjp.getActiveThreadCount());
			System.out.println("Main ForkJoinPool.getStealCount(): " +fjp.getStealCount());
			System.out.println("Main ForkJoinPool.getQueuedTaskCount(): " +fjp.getQueuedTaskCount());
			System.out.println("Main RecursiveAction.isCompletedNormally(): "+finder.isCompletedNormally());
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(!finder.isDone());
		
		fjp.shutdown();
		
		List<String> resultList = finder.join();//等待所有的任务执行完毕。
		System.out.printf("%d Files found in "+ path,resultList.size());
	}

}

class Finder extends RecursiveTask<List<String>> {

	private static final long serialVersionUID = 6338228496671403836L;

	private String extension;

	private File path;

	private static int fileListCounter;
	private static int fileCounter;
	
	public Finder(String path, String extension) {
		this.path = new File(path);
		this.extension = extension;
	}

	@Override
	protected List<String> compute() {
		fileListCounter++;
		List<String> resultList = new ArrayList<String>();
		System.out.println("resultList created"+ fileListCounter);
		List<Finder> finderList = new ArrayList<Finder>();
		System.out.println("finderList created");
		File[] contents = path.listFiles();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].isDirectory()) {
				Finder finder = new Finder(contents[i].getAbsolutePath(), extension);
				finder.fork();//asychronous
				finderList.add(finder);
			} else {//如果是文件
				if (match(contents[i].getName())) {
					String result =contents[i].getAbsolutePath();
					System.out.println(++fileCounter+"#"+result);
					resultList.add(result); 
				}
			}
		}
		addResult(resultList,finderList);
		Exception e = new Exception("=======Exception======");
		completeExceptionally(e);
		return resultList;
	}

	private boolean match(String path) {
		if (path.endsWith(extension)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void addResult(List<String> resultList,List<Finder> finderList){
		int counter = 0;
		for(Finder item:finderList){
			for(String fileName:item.join()){//join为同步的，等待执行完后才可以继续执行。
				System.out.println("addResult "+(++counter));
				resultList.add(fileName);
			}
			//resultList.addAll(item.join());//join返回List<String>,即RecursiveTask的泛型参数
			// get()方法与join方法的区别
			// join方法如果有中断，则抛出InterruptedException
			// 任务抛出任何运行时异常，get()方法抛出ExecutionException，但是join将返回RunntimeException。
		}
	}
}