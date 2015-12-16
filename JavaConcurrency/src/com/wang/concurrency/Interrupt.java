package com.wang.concurrency;

public class Interrupt {
	
	public static void main(String[] args){
		Thread task = new CaculatePrimes();
		task.start();
		try{
			Thread.sleep(10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		task.interrupt();
	}

}

class CaculatePrimes extends Thread {

	@Override
	public void run() {
		long number = 1L;
		while(true){
			if(isPrimes(number)){
				System.out.println("Prime Number is " + number );
			}
			if(this.isInterrupted()){
				System.out.println("Current Thread is interrupted");
				return;
			}
			number++;
		}

	}

	public boolean isPrimes(long number) {
		if (number < 0) {
			return false;
		} else if (number <= 2) {
			return true;
		} else {
			for (int i = 2; i < number; i++) {
				if (number % i == 0) {
					return false;
				}
			}
			return true;

		}
	}
}