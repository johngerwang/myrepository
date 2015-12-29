package com.wang.concurrency.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

//ConcurrentSkipListMap线程安全的，有序的，非阻塞map
public class ConcurrentSkipListMpTester {

	public static void main(String[] args) {
		ConcurrentSkipListMap<String, Contact> cslm = new ConcurrentSkipListMap<String, Contact>();
		Thread[] threads = new Thread[25];
		int index = 0;
		for (char i = 'A'; i < 'Z'; i++) {
			threads[index] = new Thread(new ContactTask(cslm, String.valueOf(i)));
			threads[index].start();
			index++;
		}
		for (int i = 0; i < 25; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Size of ConcurrentSkipListMap " + cslm.size());

		
		
		System.out.println("Print the first element of ConcurrentSkipListMap");

		Map.Entry<String, Contact> entry = cslm.firstEntry();
		Contact value = entry.getValue();
		String key = entry.getKey();
		System.out.println("First Elment is " + value + " : " + key);

		System.out.println("Print the last element of ConcurrentSkipListMap");

		entry = cslm.lastEntry();
		value = entry.getValue();
		key = entry.getKey();
		System.out.println("last Elment is " + value + " : " + key);

		System.out.println("Print the elements from A1003 to B1999 of ConcurrentSkipListMap");
		ConcurrentNavigableMap<String, Contact> map = cslm.subMap("A1003", "B1999");
		while (map.size() > 0) {
			entry = map.pollFirstEntry();
			if (entry != null) {
				value = entry.getValue();
				System.out.println(value);
			}
		}

	}

}

class ContactTask implements Runnable {

	private ConcurrentSkipListMap<String, Contact> cslm;
	private String id;

	public ContactTask(ConcurrentSkipListMap<String, Contact> cslm, String id) {
		super();
		this.cslm = cslm;
		this.id = id;
	}

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			Contact c = new Contact(String.valueOf(i + 1000), id);
			cslm.put(id + c.getPhone(), c);
		}
	}
}

class Contact {

	private String phone;
	private String name;

	public Contact(String phone, String name) {
		super();
		this.phone = phone;
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Contact [phone=" + phone + ", name=" + name + "]";
	}
}