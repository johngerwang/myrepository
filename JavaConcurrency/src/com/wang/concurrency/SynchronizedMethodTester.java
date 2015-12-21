package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SynchronizedMethodTester {
	public static void main(String[] args) throws InterruptedException{
		Account account = new Account(100);
		Thread bank = new Thread(new Bank(account),"Subtract Thread");
		Thread company = new Thread(new Company(account),"Add Thread");
		//Thread person = new Thread(new Person(account));
		System.out.println("init balance is : " + account.getBalance());
//		System.out.println("init name is "+ account1.getName());
		bank.start();
		company.start();
		//person.start();
		bank.join();
		company.join();
		//person.join();
		System.out.println("final balance is : " + account.getBalance());
		//System.out.println("final name is "+ account2.getName());

	}

}

class Account{
	
	private double balance;

	private static String name="wang";
	
	public static String getName(){
		return name;
	}
	private void modifyName(String myname){
		name = myname;
	}
	public static synchronized void modify(String myname){
		name = myname;
	}
	
	public void addinterest() throws InterruptedException{
		//如果不用该同步，则代码运行到第一句到时候，可能被其他线程抢占，此时到balance是之前到值，而不是最新到值，会发生balance不准确。
		//synchronized(this){ 
			double tmp = balance;
			TimeUnit.SECONDS.sleep(1);
			tmp = balance+1;
			balance = tmp;
			System.out.println(Thread.currentThread().getName()+" balance is : "+balance+" : "+ new Date());
		//}
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	public Account(double balance){
		this.balance = balance;
	}
	
	public synchronized void  add(double money) throws InterruptedException{
		double temp = balance;
		TimeUnit.SECONDS.sleep(1);
		temp+= money;
		balance = temp;
		System.out.println(Thread.currentThread().getName()+" balance is : "+balance+" : "+ new Date());
	}
	
	public synchronized void  subtract(double money) throws InterruptedException{
		double temp = balance;
		TimeUnit.SECONDS.sleep(1);
		temp-= money;
		balance = temp;
		System.out.println(Thread.currentThread().getName()+" balance is : "+balance+" : "+ new Date());

	}
	

	
}

class Bank implements Runnable{

	private Account account;
	public Bank(Account account){
		this.account = account;
	}
	@Override
	public void run() {

		for(int i=0;i<10;i++){
			try {
				account.addinterest();
				account.subtract(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		Account.modifyName("xu");
//		System.out.println("Current name"+account.getName());
	}
	
}

class Company implements Runnable{

	private Account account;
	public Company(Account account){
		this.account = account;
	}
	@Override
	public void run() {
		for(int i=0;i<10;i++){
			try {
				account.add(100);
				account.addinterest();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

class Person implements Runnable{

	private Account account;
	public Person(Account account){
		this.account = account;
	}
	@Override
	public void run() {

		for(int i=0;i<10;i++){
			try {
				account.add(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	//	Account.modifyName("Li");
	//	System.out.println("Current name"+account.getName());
	}
}