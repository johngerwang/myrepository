package com.wang.concurrency;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadCaughtException {

	public static void main(String[] args) {
		Thread pt = new Thread(new ParserTask());
		//通过下面的这个方法设置使线程能够捕捉非uncheckexception（运行时异常）捕获异常,如NumberFormatException
		//而如果是checkexception（非运行时异常）的话，可以在run()方法中捕获的。
		pt.setUncaughtExceptionHandler(new ExceptionHandler());
		//这个是线程的静态方法，该程序的所有线程都会使用该方法。
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		//运行时异常的查找的顺序
		//1.本线程的异常捕捉设置
		//2.线程组的异常捕捉设置
		//3.默认的异常捕捉设置
		pt.start();
	}

}

class ExceptionHandler implements UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("Exception Caughted");
		System.out.println(t.getName()+": " +t.getId()+" : " +t.getState());
		System.out.println("Exception Class: "+ e.getClass());
		e.printStackTrace(System.out);		
	}
}
class ParserTask implements Runnable{

	@Override
	public void run() {
		int result = Integer.parseInt("TTT");
	}
	
}