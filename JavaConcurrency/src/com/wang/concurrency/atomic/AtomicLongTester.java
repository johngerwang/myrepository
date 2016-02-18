package com.wang.concurrency.atomic;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 原子变量。对该变量的操作是原子性的，在多线程的环境下，可以达到并发安全的效果。类似的有AtomicInteger，AtomicLong,AtomicIntegerArray等。
 * 注意，Long的赋值操作是非原子的，因为Long是64位的，每一次只能赋值32位。该句话与本例子无关，顺带说一下。
 * CAS(Compare and Swap) OR (Compare and Set)
 * 即当要对某一个值进行修改时的步骤如下，1.获取内存中的值与备份的值（加载到线程的本地内存中的值，每一个线程都由一个本地内存，线程在使用变量时，从主存中获取数据到本地内存）做对比，
 * 2.如果一致，则可以修改，否则无效，继续尝试该步骤。
 * 每次做CAS操作后，都会返回当前内存中的最新值，这样就可以让第一次无效的操作继续执行下去。
 */
public class AtomicLongTester {

	public static void main(String[] args) {
		AtomicLong al = new AtomicLong();
		Account account = new Account(al);
		account.setBalance(1000);
		System.out.println("before: "+account.getBalance());
		ThreadPoolExecutor  tpe = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		Bank bank = new Bank(account);
		Company company = new Company(account);
		tpe.execute(bank);//使用execute的场合，无返回值，只能用Runnable线程。使用submit时，可以使用runnable和callable（有返回值）,有返回值Future
		tpe.execute(company);
		tpe.shutdown();
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("after: "+account.getBalance());
	}
}


class Bank implements Runnable{
	private Account account;

	public Bank(Account account) {
		super();
		this.account = account;
	}

	@Override
	public void run() {
		for(int i=0;i<10;i++){
			account.subtractAmount(10);
		}
	}
	
}
class Company implements Runnable{
	private Account account;

	public Company(Account account) {
		super();
		this.account = account;
	}
	@Override
	public void run() {
		for(int i=0;i<10;i++){
			account.addAmount(10);
		}
	}
	

}
class Account{
	
	private AtomicLong balance;
	
	public Account(AtomicLong balance){
		this.balance = balance;
	}
	
	public void setBalance(long balance){
		this.balance.set(balance);
	}
	
	public long getBalance(){
		return this.balance.get();
	}
	
	public void addAmount(long amount){
		this.balance.getAndAdd(amount);
		System.out.println("after add: " +getBalance());

	}
	
	public void subtractAmount(long amount){
		this.balance.getAndAdd(-amount);
		System.out.println("after subtract: "+ getBalance());
	} 
}


