package com.wang.concurrency.cyclicbarrier;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//使用CyclicBarrier类，使当所有的任务都完成时，才启动一个新的任务。任务数和新任务都是在该类的构造函数中定义的。
//否则因CyclicBarrier#await()方法等待。好像是每次await()被调用，则计数-1

//在使用CyclicBarrier时，需要创建一个CyclicBarrier对象，构造函数需要一个整数作为参数，
//这个参数是一个“目标”，在CyclicBarrier对象创建后，内部会有一个计数器，初始值为0，
//CyclicBarrier对象的await方法没被调用一次，这个计数器就会加1，一旦这个计数器的值达到设定的“目标”，
//所有被CyclicBarrier.await阻塞住的线程都会继续执行。这个目标是固定的，一旦设定便不能修改。 
//举一个形象的例子，假设有5个人爬山，他们要爬到山顶，等到5个人到期了在同时出发下山，
//那么我们要在山顶设定一个“目标”，同时还有一个计数器，这个目标就是5，每到山顶一个人，
//这个人就要等待，同时计数器加1，等到5个人到齐了，也就是计数器达到了这个“目标”，所有等待的人就开始下山了。

//CyclicBarrier和CountDownLatch的共同点都是有一个目标和一个计数器，等到计数器达到目标后，
//所有阻塞的线程都将继续执行。他们的不同点是CyclicBarrier.await在等待的同时还修改计数器，
//而CountDownLatch.await只负责等待，CountDownLatch.countDown才修改计数器，
//这是他们最本质的区别（千万不要告诉我他们的区别是一个加1一个减1！这就像数数，从1数到10和从10数到1都是数10个数，没有区别的）。
public class CyclicBarrierTester {

	public static void main(String[] args) throws InterruptedException {
		int row = 10000;
		int column = 10000;
		int searchNumber =6;
		int searchRows = 200;
		int participant =5;
		MatrixMocker mm = new MatrixMocker(row,column,searchNumber);
		Result rt = new Result(row);
		Grouper gp = new Grouper(rt);
		CyclicBarrier cb = new CyclicBarrier(participant,gp); //当所有的participant都完成后（向下计数(-1)）即等于0，才会调用gp线程执行

		long before = System.currentTimeMillis();
		Thread[] ts = new Thread[participant] ;
		for(int i=0;i<participant;i++){
			ts[i] = new Thread(new Searcher(i*searchRows,i*searchRows+searchRows,searchNumber,rt,cb,mm));
			ts[i].start();
		}
		for(int i=0;i<participant;i++){
			ts[i].join();
		}
		long after = System.currentTimeMillis();
		System.out.println("Search finished in " + (after-before)+ "mills");
	}

}

class MatrixMocker{
	private int data[][];
	private int counter;
	
	public MatrixMocker(int row,int column,int number){
		this.counter = 0;
		this.data = new int[row][column];
		Random rm = new Random();
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				data[i][j]= rm.nextInt(10);
			}
		}
		long before = System.currentTimeMillis();
		System.out.println("Current Time before search: " +new Date());
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				if(data[i][j]==number){
					counter++;
				}
			}
		}
		long after = System.currentTimeMillis();
		System.out.println("Current Time after search: " +new Date());
		System.out.println("There are "+counter+" matches in "+ (after-before));//获取一次查找的毫秒数
//		System.out.println("===========data================");
//		for(int i=0;i<row;i++){
//			for(int j=0;j<column;j++){
//					System.out.println(data[i][j]);
//				
//			}
//		}
//		System.out.println("===========data================");

	}
	
	
	public int[] getRow(int rowNumber){
		if((rowNumber>=0)&&(rowNumber<data.length)){
			return data[rowNumber];
		}
		return null;
	}
}

class Result{
	private int[] matchs;
	
	public Result(int size){
		this.matchs = new int[size];
	}
	
	public void setResult(int result,int position){
		matchs[position] = result;
	}
	
	public int[] getResult(){
		return matchs;
	}
}

class Searcher implements Runnable{

	private int startRow;
	private int endRow;
	private int searchNumber;
	private Result result;
	private final CyclicBarrier  cb;
	private MatrixMocker mm;
	
	public Searcher(int startRow,int endRow,int searchNumber,Result result,CyclicBarrier cb,MatrixMocker mm){
		this.searchNumber = searchNumber;
		this.startRow = startRow;
		this.endRow = endRow;
		this.result = result;
		this.cb =cb;
		this.mm =mm;
	}
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+ " search start");
		for(int i=startRow;i<endRow;i++){
			int counter = 0;
			int data[] = mm.getRow(i);
			for(int j=0;j<data.length;j++){
				if(searchNumber == data[j]){
					counter++;
				}
			}
			result.setResult(counter, i);
		}
		System.out.println(Thread.currentThread().getName()+ " processed "+(endRow-startRow)+" lines.startRow :"+startRow+" endRow : "+endRow);
		try {
			cb.await();//当处理结束后，使该线程休眠，直到定义的CyclicBarrier对象的线程都已结束，才会唤醒线程。
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
			//当重置（reset方法）CyclicBarrier对象时，会抛出BrokenBarrierException异常，此时可以做一些根据实际需要的操作。
			//CyclicBarrier有一个Broken（损坏）的状态，当出现损坏（如中断了某一个线程），则其他的线程会抛出BrokenBarrierException的异常。
		}
	}
}

class Grouper implements Runnable{

	private Result result;
	private int matches = 0;
	
	public Grouper(Result result){
		this.result = result;
	}
	@Override
	public void run() {
		for(int match:result.getResult()){
			matches+=match;
		}
		System.out.println("total matches is "+ matches);
	}
}