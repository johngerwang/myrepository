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
 * 利用该辅助类CompletionService，能够实现在A线程中执行任务，在B线程中获取任务执行的结果。
 */
public class CompletionServiceTester {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		CompletionService<String> cs = new ExecutorCompletionService<String>(es);
		ReportRequester onlineRR = new ReportRequester("OnLine", cs);
		ReportRequester faceRR = new ReportRequester("Face", cs);
		ResultProcessor resultProcessor = new ResultProcessor(cs,false);

		ExecutorService es1 = Executors.newCachedThreadPool();
		es1.execute(onlineRR);
		es1.execute(faceRR);
		es1.execute(resultProcessor);
		try {
			TimeUnit.SECONDS.sleep(20);
			resultProcessor.setEnd(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		es1.shutdown();
		es.shutdown();
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
		cs.submit(rg);
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