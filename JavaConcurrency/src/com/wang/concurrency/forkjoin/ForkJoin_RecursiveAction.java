package com.wang.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 使用Fork/Join框架，执行分而治之的任务。Fork是分解，Join是合并操作。Join可以理解为ForkJoinPool。Fork可以理解为RecursiveAction/RecursiveTask
 * 主任务：PriceIncreamentTask extends RecursiveAction，参考大小在该类的compute中根据实际情况设定，本例子中使用的是10。
 * 执行该主任务的线程叫做Worker Thread（工作者线程），该线程查找所有所有未被执行的任务,任务由RecursiveAction根据参考大小来分解。
 * 注意：不要在task中执行主任务。
 * 主任务分成若干个子任务，子任务完成后，主任务才完成。此时才join。
 * 和CyclicBarrier类似，但是这个Fork/Join框架更主动，更加自我管理，不需要自己去创建线程
 */
public class ForkJoin_RecursiveAction {

	public static void main(String[] args) {
		//默认创建与大小为cpu数的线程池
		//ublic class ForkJoinPool extends AbstractExecutorService 
		ForkJoinPool fjp = new ForkJoinPool();
		ProductManager pm = new ProductManager();
		List<ProductManager.Product> plist = pm.generator(1000);
		PriceIncreamentTask pit = new PriceIncreamentTask(0,plist.size(),0.2,plist);
		
		//此处也可以不使用RecursiveAction，使用Runnable对象也可以，但是使用了Runnable的话，就不会使用"工作窃取算法",窃取其他线程未完成的任务队列中的任务。
		//此方法是异步的，即执行到该语句后，就往下执行，而invoke相关方法则是同步的，不执行完不能往下走。
		//也可以使用invokeAll,invokeAny，但是只接受Callable类型参数。
		fjp.execute(pit);
		do{
			System.out.println("Main ForkJoinPool.getParallelism(): "+fjp.getParallelism());
			System.out.println("Main ForkJoinPool.getActiveThreadCount(): " +fjp.getActiveThreadCount());
			System.out.println("Main ForkJoinPool.getStealCount(): " +fjp.getStealCount());
			System.out.println("Main RecursiveAction.isCompletedNormally(): "+pit.isCompletedNormally());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(!pit.isDone());
		fjp.shutdown();
		
		for(int i=0;i<plist.size();i++){
			if(plist.get(i).getPrice()!=12){
				System.out.println("Cannot be Modified: "+plist.get(i));
			}
		}
		System.out.println("Main End");
	}

}
//public abstract class RecursiveAction extends ForkJoinTask<Void>
//该类无返回结果。RecursiveTask类有返回结果。
class PriceIncreamentTask extends RecursiveAction{

	/**
	 * 因为RecursiveAction是序列化的类，所以此处必须要有serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int start;
	private int end;
	private double increment;
	private List<ProductManager.Product> plist;
	private int counter;
	public PriceIncreamentTask(int start,int end,double increment,List<ProductManager.Product> plist){
		this.end = end;
		this.start =start;
		this.increment = increment;
		this.plist = plist;
	}
	
	//注意，此方法不能抛出非运行时异常，必须捕获。
	@Override
	protected void compute() {
		System.out.println("PriceIncreamentTask#"+(counter++)+" start: " + start + "_end: " + end);
		if(end-start<10){
			updatePrices();
		}else{
			int middle = (start+end)/2;
			System.out.println("PriceIncreamentTask Pending : " + getQueuedTaskCount());
			PriceIncreamentTask pit1 = new PriceIncreamentTask(start,middle,increment,plist);
			PriceIncreamentTask pit2 = new PriceIncreamentTask(middle,end,increment,plist);
			invokeAll(pit1, pit2);
		}
	}
	
	public void updatePrices(){
		for(int i=start;i<end;i++){
			ProductManager.Product product = plist.get(i);
			product.setPrice(product.getPrice()*(1+increment));
		}
	}
}

class ProductManager{
	
	private List<Product> plist;
	
	public ProductManager(){
		plist = new ArrayList<Product>();
	}
	
	public List<Product> generator(int size){
		for(int i=0;i<size;i++){
			plist.add(new Product("Product"+i,10));
		}
		return plist;
	}
	
	public List<Product> get(){
		return plist;
	}
	
	public class Product{
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		private String name;
		private double price;
		
		public Product(String name,double price){
			this.name = name;
			this.price = price;
		}
		
		public String toString(){
			return name+"_"+price;
		}
	}
}
