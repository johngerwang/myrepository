package com.wang.concurrency.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

//通过Exchanger辅助类来交换数据。
//主要是Exchanger的exchange()方法。执行到该方法后，线程等待，数据交换完毕后，继续执行
public class ExchangerTester {

	public static void main(String[] args) {
		Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
		List<String> producerBuffer = new ArrayList<String>();
		List<String> consumerBuffer = new ArrayList<String>();
		Thread producer = new Thread(new Producer(producerBuffer,exchanger));
		Thread consumer = new Thread(new Consumer(consumerBuffer,exchanger));
		producer.start();
		consumer.start();

	}

}

class Producer implements Runnable{

	private List<String> buffer;
	private Exchanger<List<String>> exchanger;
	
	public Producer(List<String> buffer, Exchanger<List<String>> exchanger){
		this.buffer = buffer;
		this.exchanger = exchanger;
		
	}
	@Override
	public void run() {
		int cycle = 1;
		for(int i =0;i<10;i++){
			for(int j=0;j<5;j++){
				buffer.add("Event#"+(i*10+j));
			}
			System.out.println("Producer: cycle#" +cycle+". Before Exchange,Producer buffer size: "+buffer.size());
			try {
				buffer = exchanger.exchange(buffer);//将生产出来的buffer传递给消费者，将消费者的buffer返回给生产者。
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Producer: cycle#" +cycle+". After Exchange,Producer buffer size: "+buffer.size());
			cycle++;
		}
	}
	
}

class Consumer implements Runnable{

	private List<String> buffer;
	private Exchanger<List<String>> exchanger;
	
	public Consumer(List<String> buffer, Exchanger<List<String>> exchanger){
		this.buffer = buffer;
		this.exchanger = exchanger;
		
	}
	@Override
	public void run() {
		int cycle = 1;
		for(int i =0;i<10;i++){
			try {
				buffer = exchanger.exchange(buffer);//将消费过的buffer传递给生产者，此时buffer的长度已经为0了。
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Consumer: cycle#" +cycle+". After exchange,Consumer buffer size: "+buffer.size());
			for(int j=0;j<5;j++){
				System.out.println("Consumer: cycle#" +cycle+" "+buffer.get(0));
				buffer.remove(0);
			}
			cycle++;
		}
	}
	
}
