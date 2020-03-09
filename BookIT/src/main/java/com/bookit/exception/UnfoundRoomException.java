package com.bookit.exception;

public class UnfoundRoomException extends Exception {
	private final String room;
	
	public UnfoundRoomException(String room) {
		this.room = room;
	}

	public String getRoom() { return room; }

}
