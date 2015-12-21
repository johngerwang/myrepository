package com.wang.concurrency;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SynchronizedTester {
	public static void main(String[] args) throws InterruptedException{
		Account account1 = new Account(100);
		Account account2 = new Account(100);
		Thread bank = new Thread(new Bank(account1));
		Thread company = new Thread(new Company(account1));
		Thread person = new Thread(new Person(account2));
		System.out.println("init balance is : " + account1.getBalance());
		System.out.println("init name is "+ account1.getName());
		bank.start();
		person.start();
		
		bank.join();
		//company.join();
		person.join();
		System.out.println("final balance is : " + account1.getBalance());
		System.out.println("final name is "+ account2.getName());

	}

}

class Account{
	
	private double balance;

	private static String name="wang";
	
	public static String getName(){
		return name;
	}
	public static synchronized void modifyName(String myname){
		name = myname;
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
		//System.out.println(Thread.currentThread().getName()+" after add,balance is : "+balance+". add time : "+ new Date());

	}
	
	public synchronized void  subtract(double money) throws InterruptedException{
		double temp = balance;
		TimeUnit.SECONDS.sleep(1);
		temp-= money;
		balance = temp;
		//System.out.println("after subtract,balance is : "+balance+ ". subtract time : "+ new Date());
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
				account.subtract(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Account.modifyName("xu");
		System.out.println("Current name"+account.getName());
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
		Account.modifyName("Li");
		System.out.println("Current name"+account.getName());
	}
}