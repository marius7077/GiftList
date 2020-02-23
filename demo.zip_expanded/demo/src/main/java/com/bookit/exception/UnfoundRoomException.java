package com.bookit.exception;

public class UnfoundRoomException extends Exception {
	private String room;
	
	public UnfoundRoomException(String room) {
		this.room = room;
	}

	public UnfoundRoomException() {}

	public String getRoom() { return room; }

}
