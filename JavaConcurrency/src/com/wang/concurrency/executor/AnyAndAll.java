package com.wang.concurrency.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

//invokeAny：设计的目的是当多个线程都执行同一个目的的操作时，只要一个执行成功，就可以放弃其他的执行了，这样的场景下可以使用该方法。
//一般可以通过当调用不成功时抛出异常来处理。
public class AnyAndAll {

	public static void main(String[] args) {
		UserValidator uvLdap = new UserValidator("Ldap");
		UserValidator uvDB = new UserValidator("DB");

		ValidatorTask vt1 = new ValidatorTask(uvLdap,"test","password");
		ValidatorTask vt2 = new ValidatorTask(uvDB,"test","password");

		List<ValidatorTask> vtList = new ArrayList<ValidatorTask>();
		vtList.add(vt1);
		vtList.add(vt2);
		
		ThreadPoolExecutor tpe = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		System.out.println("================InvokeAny Start================");
		String result =null;
		try {
			//获得任何一个线程的执行结果,然后就可以继续往下执行了，否则等待。
			//获得结果按照以下规则
			//1.如果所有都不成功（一般会不成功的处理是线程抛出异常），那么此处抛出异常，无需理会是谁抛出的异常）
			//2.如果都成功，那么此处返回任何一个结果。
			//3.如果一个成功，一个不成功（有异常抛出），那么实际不会抛出异常，只是会返回成功的结果。
			result	 = tpe.invokeAny(vtList);
			System.out.println("ANY:ValidatorTask Result: " + result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
		}finally{
			
		}
		//tpe.shutdown();
		System.out.println("================InvokeAny End================");
		
//		System.out.println("================InvokeAll Start================");
//		List<Future<String>>  resultList = null;
//		try {
//			 resultList =tpe.invokeAll(vtList);//此处返回的是list。必须所有的返回值都获得了才能继续往下执行。
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int i=0;i<resultList.size();i++){
//			try {
//				System.out.println("ALL:ValidatorTask Result "+ resultList.get(i).get());
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
//		}
		tpe.shutdown();
		System.out.println("InvokeAll End");

	}
	
		

	
	
	
	
}

class UserValidator{
	
	private String validatorName;
	
	public UserValidator(String validatorName){
		this.validatorName = validatorName;
	}
	
	public boolean validate(String username,String password){
		Random rm = new Random();
		if(rm.nextBoolean()){
			System.out.println("Validate OK "+validatorName+" [username=" + username +" ,password= "+ password+"]");
			return true;
		}else{
			return false;
		}
	}
		
	public String getName(){
		return this.validatorName;
	}
}


class ValidatorTask implements Callable<String>{

	private UserValidator uv;
	
	private String username;
	private String password;
	
	
	@Override
	public String toString() {
		return "ValidatorTask [uv=" + uv + ", username=" + username + ", password=" + password + "]";
	}

	public ValidatorTask(UserValidator uv,String username,String password){
		this.uv = uv;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String call() throws Exception {
		if(!uv.validate(username, password)){
			System.out.println("can not found," + uv.getName());
			throw new Exception("Exception can not found," + uv.getName());//如果抛出异常，则不再往下执行
		}
		System.out.println("can be found," + uv.getName());
		return uv.getName();
	}
}


