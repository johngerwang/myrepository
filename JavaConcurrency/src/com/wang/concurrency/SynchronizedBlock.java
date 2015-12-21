package com.wang.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SynchronizedBlock {

	public static void main(String[] args) throws InterruptedException {
		Room ra = new Room(1, "A", 20);
		Room rb = new Room(1, "B", 20);
		List<Room> roomlist = new ArrayList<Room>();
		roomlist.add(ra);
		roomlist.add(rb);
		Cinema cinema = new Cinema(roomlist);
		Thread t1 = new Thread(new Saler(cinema));
		Thread t2 = new Thread(new Saler(cinema));
		t1.start();
		t2.start();
//		t1.join();
//		t2.join();
		System.out.println("final "+ra.getName() + " left seats " + ra.getSeats());
		System.out.println("final "+rb.getName() + " left seats " + rb.getSeats());
		t1.interrupt();
		t2.interrupt(); //synchronizd同步方法依然也是可以被打断的
		System.out.println("t1.interruped "+t1.isInterrupted());
		System.out.println("t2.interruped "+t2.isInterrupted());

	}
}

class Room {

	private String name;
	private int id;
	private int seats;

	public Room(int id, String name, int seats) {
		this.id = id;
		this.name = name;
		this.seats = seats;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public void modifySeats(int seats) {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class Cinema {

	private List<Room> roomList;

	public Cinema(List<Room> myroomList) {
		this.roomList = myroomList;
	}

	public List<Room> getRooms() {
		return roomList;
	}

	public boolean sellTicket(Room room, int amount) {
		synchronized (room) {
			try{
			if (room.getSeats() > amount) {
				room.setSeats(room.getSeats() - amount);
				System.out.println(room.getName() +" left seats " + room.getSeats());
				TimeUnit.SECONDS.sleep(1);
				return true;
			}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			return false;
		}
	}

	public boolean returnTicket(Room room, int amount) {
		synchronized (room) {
			try{
			room.setSeats(room.getSeats() + amount);
			System.out.println(room.getName() +" left seats " + room.getSeats());
			TimeUnit.SECONDS.sleep(1);
			return true;
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			return false;
		}
	}
}

class Saler implements Runnable {

	private Cinema cinema;

	public Saler(Cinema ca) {
		this.cinema = ca;
	}

	@Override
	public void run() {
		try {
			cinema.sellTicket(cinema.getRooms().get(1), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.sellTicket(cinema.getRooms().get(0), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.sellTicket(cinema.getRooms().get(1), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.sellTicket(cinema.getRooms().get(0), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.returnTicket(cinema.getRooms().get(1), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.returnTicket(cinema.getRooms().get(0), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.returnTicket(cinema.getRooms().get(1), 2);
			TimeUnit.SECONDS.sleep(1);
			cinema.returnTicket(cinema.getRooms().get(0), 2);
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}