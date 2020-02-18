package com.bookit.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.service.JSONManager;

@Controller
public class RoomController {
	
	@Autowired
	private BookController bookCtrl;
	@Autowired
	private JSONManager json;

	public List<Room> list(Command command) throws ParseException {
		if(command.getAllOption()) return json.readRooms();		
		return getAllRoomsByOptions(command.getStartDate(), command.getEndDate(), command.getClosedOption(), command.getITOption(), command.getNbPers());
	}

	public Room describe(Command command) {
		return getRoomByName(command.getRoom());
	}
	
	public boolean checkBookability(Command command) {
		Room room = getRoomByName(command.getRoom());

		if(room.getBookList() != null) {
			List<Book> booksInInterval = room.getBookList().stream().filter(b -> bookCtrl.inInterval(b, command.getStartDate(), command.getEndDate())).collect(Collectors.toList());
			if((command.getClosedOption() || command.getPrivateOption()) && !booksInInterval.isEmpty()) return false;
		}
		return true;
	}

	public boolean book(String roomName, Book book) {
		List<Room> rooms = json.readRooms();
		rooms.stream().filter(r -> roomName.equals(r.getName())).findFirst().orElse(new Room()).getBookList().add(book);
		return json.writeRooms(rooms);
	}
	
	private List<Room> getAllRoomsByOptions(long start, long end, boolean closed, boolean it, int nbPlaces) {
		return json.readRooms().stream().filter(r -> r.isItroom() == it 
													&& r.getCapacity() >= nbPlaces
													&& isAvailable(r, start, end, closed)).collect(Collectors.toList());
	}
	
	public boolean isAvailable(Room room, long start, long end, boolean closed) {
		if(room.getBookList() != null) {
			List<Book> booksInInterval = room.getBookList().stream().filter(b -> bookCtrl.inInterval(b, start, end)).collect(Collectors.toList());
			if(closed && !booksInInterval.isEmpty()) return false;
		}
		return true;
	}

	private Room getRoomByName(String room) {
		return json.readRooms().stream().filter(r -> room.equals(r.getName())).findFirst().orElse(new Room());
	}

	public boolean isOpened(Room room, long start) {
		if(room.getBookList() != null) {
			List<Book> booksInInterval = room.getBookList().stream().filter(b -> bookCtrl.inInterval(b, start, start + 1)).collect(Collectors.toList());
			if(!booksInInterval.isEmpty()) return true;
		}
		return false;
	}
}
