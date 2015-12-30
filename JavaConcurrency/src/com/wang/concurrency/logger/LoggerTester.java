package com.wang.concurrency.logger;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

//该java.util.logging.Logger是线程安全的。

public class LoggerTester {

	public static void main(String[] args) {
		Logger logger = MyLogger.getLogger("Core");
		logger.entering("Core", "main()",args);
		Thread[] threads = new Thread[5];
		for(int i=0;i<threads.length;i++){
			logger.log(Level.INFO, "Lauching thread#"+i);
			Task task = new Task();
			threads[i] = new Thread(task);
			logger.log(Level.INFO, "Thread created: "+threads[i].getName());
			threads[i].start();
		}
		logger.log(Level.INFO,"10 Threads Created.");		
		for(int i=0;i<threads.length;i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Exception", e);
			}
		}
		logger.exiting("Core", "main");
		
	}

}

class MyFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		sb.append("[" + record.getLevel() + "]-");
		sb.append(new Date(record.getMillis()) + " : ");
		sb.append(record.getSourceClassName() + "." + record.getSourceMethodName());//发送消息的类名+发送消息的方法名
		sb.append(record.getMessage() + "\n");
		return sb.toString();
	}

}

class MyLogger {
	private static Handler handler;

	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.ALL);
		if (handler == null) {
			try {
				handler = new FileHandler("recipe8.log");
			} catch (SecurityException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Formatter formatter = new MyFormatter();
			handler.setFormatter(formatter);
		}
		if (logger.getHandlers().length == 0)// 如果没有一个处理程序与Logger对象关联
		{
			logger.addHandler(handler);
		}
		return logger;
	}
}

class Task implements Runnable {

	@Override
	public void run() {
		Logger logger = MyLogger.getLogger(this.getClass().getName());
		logger.entering(Thread.currentThread().getName(), "run()");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.exiting(Thread.currentThread().getName(), "run()",Thread.currentThread());
	}

}