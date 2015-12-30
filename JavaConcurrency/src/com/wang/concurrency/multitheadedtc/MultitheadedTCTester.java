package com.wang.concurrency.multitheadedtc;

import java.util.Date;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;

public class MultitheadedTCTester {

	public static void main(String[] args) {

		ProducerConsumerTest test = new ProducerConsumerTest();
		System.out.println("Main:Starting the test");
		try {
			TestFramework.runOnce(test);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("Main: The test finished");
	}

}

class ProducerConsumerTest extends MultithreadedTestCase{
	
	private LinkedTransferQueue<String> queue;
	
	@Override
	public void initialize(){
		super.initialize();
		queue = new LinkedTransferQueue<String>();
		System.out.println("Test: the test has been initialized");
	}
	
	public void thread1() throws InterruptedException{//必须以thread作为方法名前缀，会创建一个线程
		System.out.println("thread1() enter current tick: "+this.getTick());
		String ret = queue.take();
		System.out.println("thread1() queue.take() current tick: "+this.getTick());
		System.out.println("Thread 1: "+ret);
	}
	public void thread2() throws InterruptedException{
		System.out.println("thread2():enter current tick: "+this.getTick());
		waitForTick(1);//在take()方法中使用该方法等待直至第一个线程休眠？
		System.out.println("thread2() waitForTick(1) current tick: "+this.getTick());
		String ret = queue.take();
		System.out.println("thread2() queue.take() current tick: "+this.getTick());
		System.out.println("Thread 2: "+ret);
	}
	
	public void thread3() throws InterruptedException{
		System.out.println("thread3() enter current tick: "+this.getTick());
		//使用该方法来控制线程的执行顺序，使运行测试的所有线程都被阻塞，然后MultithreadTC库调用waitForTick方法来恢复被阻塞的线程
		waitForTick(1);
		System.out.println("thread3() waitForTick(2) current tick: "+this.getTick());
		queue.put("Event1");
		queue.put("Event2");
		System.out.println("thread3() queue.put current tick: "+this.getTick());
		System.out.println("Thread 3: insert 2 elements");
	}
	
	public void finish(){
		super.finish();
		System.out.println("Test: end");
		assertTrue(queue.size()==0);
		

	}
}
