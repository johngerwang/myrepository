package com.wang.concurrency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Conditon {

	public static void main(String[] args) {
		FileMocker fm1 = new FileMocker(4, 10);
		FileMocker fm2 = new FileMocker(4, 10);
		List<FileMocker> fmList = new ArrayList<FileMocker>();
		fmList.add(fm1);
		fmList.add(fm2);
		Buffer bf = new Buffer(2);
		Thread producer1 = new Thread(new AProducer(fmList, bf), "producer1");
		Thread producer2 = new Thread(new AProducer(fmList, bf), "producer2");

		// for (int i = 0; i < 2; i++) {
		Thread consumer = new Thread(new AConsumer(bf), "Consumer");

		consumer.start();
		// }
		producer1.start();
		producer2.start();
	}

}

class FileMocker {

	private String contents[];
	private int currentIndex;

	public FileMocker(int row, int column) {
		contents = new String[row];
		for (int i = 0; i < row; i++) {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < column; j++) {
				sb.append('a');
			}
			contents[i] = sb.toString();
		}
		currentIndex = 0;
	}

	public boolean hasMoreLines() {
		return currentIndex < contents.length;
	}

	public String getLine() {
		if (this.hasMoreLines()) {
			return contents[currentIndex++];
		}
		return "No Lines";
	}
}

class Buffer {
	private LinkedList<String> buffer;
	private int bufferMaxSize;
	private ReentrantLock lock = new ReentrantLock();;
	private Condition canInsert;// 是否能够插入
	private Condition canGet;// 是否能够获取
	private static int size = 0;
	 private boolean MoreData =true;
	// //是否还有数据存在buffer中，初始值得是true。这样即使第一次没有数据，消费者依然能进来等待，否则不会等待，参考消费者的run()方法。
	private int readCounter;
	private int insertCounter;
	

	public Buffer(int maxSize) {
		this.bufferMaxSize = maxSize;
		this.buffer = new LinkedList<String>();
		this.canInsert = lock.newCondition();
		this.canGet = lock.newCondition();
	}

	 public void setMoreData(boolean moreData){
	 this.MoreData = moreData;
	 }
	
	 public boolean getNoMoreData(){
	 return this.MoreData;
	 }
	public int getBufferMaxSize() {
		return bufferMaxSize;
	}

	public int getSize() {
		return size;
	}

	public void insert(String line) {
		lock.lock();
		try {
			while (buffer.size() >= this.bufferMaxSize) {
				System.out.println("==========FULL===========");
				canInsert.await();
			}
			buffer.offer(line);
			System.out.println("InsertCounter is " + ++insertCounter);
			size++;
			System.out.println(Thread.currentThread().getName() + " after add  to buffer,size is: " + buffer.size());
			canGet.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
			System.out.println(Thread.currentThread().getName() + " release the lock in insert()");

		}

	}

	public String get() {
		String line = null;
		lock.lock();
		try {
			while (buffer.size() == 0) {
				System.out.println("===========EMPTY===============");
				canGet.await();
			}
			line = buffer.poll();
			System.out.println("ReadCounter is " + ++readCounter);
			size--;
			System.out
					.println(Thread.currentThread().getName() + " ：after remove from buffer,size is " + buffer.size());
			canInsert.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
			System.out.println("release the lock in get()");
		}
		return line;
	}
}

class AProducer implements Runnable {

	private List<FileMocker> filemklist;
	private Buffer buffer;

	public AProducer(List<FileMocker> fmlist, Buffer bf) {
		this.filemklist = fmlist;
		this.buffer = bf;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Started");
		Iterator<FileMocker> it = filemklist.iterator();
		while (it.hasNext()) {
			FileMocker fm = it.next();
			while (fm.hasMoreLines()) {
				buffer.insert(fm.getLine());
			}
		}
		buffer.setMoreData(false);
		System.out.println(Thread.currentThread().getName() + " end");
	}
}

class AConsumer implements Runnable {

	private Buffer bf;

	public AConsumer(Buffer bf) {
		this.bf = bf;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Started");
		while (bf.getNoMoreData()) { // 当第一次进入的时候，由于没有数据存在，如果用bf.getSize()>0的判断方式的话，就进不来等待了。
			// 所以此处是一个特别的处理方式，就是只要当FileMocker中有未读完的数据存在，那就进入调用buffer的canGet条件的等待队列，即调用get()方法去读。
			// 也可以用bf.getSize()>=0的判断条件，让消费者进入buffer的canGet条件等待队列
			bf.get();
		}
		System.out.println("AConsumer end");
	}
}
