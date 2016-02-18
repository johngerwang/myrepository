package com.wang.concurrency.completion.service;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 该类的目的就是用来在一个线程中获取所有其他线程中产生的数据
 * 利用该辅助类CompletionService，能够实现在A线程中执行任务，在B线程中获取任务执行的结果。
 * 当使用ExecutorService启动了多个Callable后，每个Callable会产生一个Future，
 * 我们需要将多个Future存入一个线性表，用于之后处理数据。
 * 当然，还有更复杂的情况，有5个生产者线程，每个生产者线程都会创建任务，
 * 所有任务的Future都存放到同一个线性表中。另有一个消费者线程，从线性表中取出Future进行处理
 */
public class CompletionServiceTester {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		//CompletionService的子类。泛型参数是返回值的类型。
		//当completionService的submit方法执行时，相当于是委托给ExecutorService线程池中的线程去执行指定的任务，		
		//参考85行，cs.submit(rg);//提交任务，使ExecutorService执行之。
		CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
		ReportRequester onlineRR = new ReportRequester("OnLine", completionService);
		ReportRequester faceRR = new ReportRequester("Face", completionService);
		ResultProcessor resultProcessor = new ResultProcessor(completionService,false);

		//执行打印任务的线程池
		//ExecutorService es1 = Executors.newCachedThreadPool();
		executor.execute(onlineRR);
		executor.execute(faceRR);
		executor.execute(resultProcessor);
		try {
			TimeUnit.SECONDS.sleep(10);
			resultProcessor.setEnd(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//es.shutdown();
		executor.shutdown();
	}

}
//打印并返回打印结果
class ReportGenerator implements Callable<String> {

	private String title;
	private String requester;

	public ReportGenerator(String title, String requester) {
		this.title = title;
		this.requester = requester;
	}

	@Override
	public String call() throws Exception {
		System.out.println("Start Printing");
		TimeUnit.SECONDS.sleep(new Random().nextInt(5));
		System.out.println("End Printing: " + requester + "_" + title);
		return requester + "_" + title;
	}

}

//调用打印任务，进行打印
class ReportRequester implements Runnable {

	private String name;

	private CompletionService<String> cs;

	public ReportRequester(String name, CompletionService<String> cs) {
		this.name = name;
		this.cs = cs;
	}

	@Override
	public void run() {
		ReportGenerator rg = new ReportGenerator(name, "报销");
		cs.submit(rg);//提交任务，使执行。
	}
}

//获取打印结果
class ResultProcessor implements Runnable {

	private CompletionService<String> cs;
	private boolean isEnd;

	public ResultProcessor(CompletionService<String> cs, boolean end) {
		this.cs = cs;
		this.isEnd = end;
	}

	@Override
	public void run() {
		while (!isEnd) {
			try {
				//在结果的Future的列表中查看，如果是空的，则等待20秒。
				//注意，是Future列表。CompletionService内部使用的是BlockingQueue<Future<V>>
				//如果是无参数的poll，则如果是空，立刻返回。
				//take()方法则死守，直到非空为止
				//注意：返回的结果在Future队列中。
				
				Future<String> fs = cs.poll(20, TimeUnit.SECONDS);
				if (fs != null) {
					System.out.println("Result is : " + fs.get());
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setEnd(boolean end){
		this.isEnd = end;
	}
}