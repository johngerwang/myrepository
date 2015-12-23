package com.wang.concurrency.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
		String result =null;
		try {
			result	 = tpe.invokeAny(vtList);
			System.out.println("ValidatorTask Result: " + result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
		}finally{
			
		}
		//tpe.shutdown();
		System.out.println("InvokeAny End");
		List<Future<String>>  resultList = null;
		try {
			 resultList =tpe.invokeAll(vtList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<resultList.size();i++){
			try {
				System.out.println(resultList.get(i).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
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
			System.out.println("Validate OK " + username +" : "+ password);
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
	
	
	public ValidatorTask(UserValidator uv,String username,String password){
		this.uv = uv;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String call() throws Exception {
		if(!uv.validate(username, password)){
			System.out.println("can not found," + uv.getName());
			throw new Exception("Exception can not found," + uv.getName());
		}
		System.out.println("can be found," + uv.getName());
		return uv.getName();
	}
}


