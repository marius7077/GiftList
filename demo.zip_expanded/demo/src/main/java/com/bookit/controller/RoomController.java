package com.bookit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bookit.exception.UnfoundRoomException;
import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.service.JSONManager;

@Controller
public class RoomController {
	
	@Autowired
	private BookController bookCtrl;
	@Autowired
	private JSONManager json;

	/**
	 * Get a room list to display according to the command options
	 * @param command : The command with all information about which rooms have to be displayed
	 * @see Command
	 * @return List<Room> : The computed list of rooms
	 */
	public List<Room> list(Command command) {
		if(command.getAllOption()) return json.readRooms();		
		return getAllRoomsByOptions(
			command.getStartDate(), 
			command.getEndDate(), 
			command.getClosedOption(), 
			command.getITOption(), 
			command.getNbPers()
		);
	}
	
	/**
	 * Check if it is possible to book a room for a specific date
	 * @param command : The command which contains room and dates information
	 * @return
	 */
	public boolean checkBookability(Command command) {
		Room room = command.getRoom();

		if(!room.getBookList().isEmpty()) {
			List<Book> booksInInterval = bookCtrl.getBooksInInterval(
					room, 
					command.getStartDate(), 
					command.getEndDate()
			);
			
			if(!booksInInterval.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add a new Book object to the room's booklist
	 * @param room : The room we want to book
	 * @param book : The Book object
	 * @return boolean : true if sucess
	 */
	public boolean book(Room room, Book book) {
		List<Room> rooms = json.readRooms();
		rooms.stream()
			.filter(r -> room.getName().equals(r.getName()))
			.findFirst().orElse(new Room())
			.getBookList()
			.add(book);
		return json.writeRooms(rooms);
	}
	
	/**
	 * Get a room list to display according to parameters
	 * @param start : Interval of time's start in which we want to know accessible rooms in millisecond
	 * @param end : Interval of time's end in which we want to know accessible rooms in millisecond
	 * @param closed : If true, get only closed rooms
	 * @param it : If true, get only IT rooms
	 * @param nbPlaces : Minimal size for returned rooms
	 * @return List<Room> : The computed list of rooms
	 */
	private List<Room> getAllRoomsByOptions(
		long start, 
		long end, 
		boolean closed, 
		boolean it, 
		int nbPlaces
	) {
		return json.readRooms().stream()
				.filter(r -> 
					(!it || r.isItroom())
					&& r.getCapacity() >= nbPlaces
					&& isAccessible(r, start, end, closed)
				).collect(Collectors.toList());
	}
	
	/**
	 * Check if a room is available at a time, according to close option and room's book's privacy
	 * @param room : The room
	 * @param start : Interval of time's start in which we want to know if the room is available in millisecond
	 * @param end : Interval of time's start in which we want to know if the room is available in millisecond
	 * @param closed : If true, the method will return false if the room is already booked, even if it is accessible
	 * @return boolean : true if the room is accessible, false if not
	 */
	public boolean isAccessible(Room room, long start, long end, boolean closed) {
		if(!room.getBookList().isEmpty()) {
			List<Book> booksInInterval = bookCtrl.getBooksInInterval(room, start, end);
			if(closed && !booksInInterval.isEmpty()) return false;
		}
		return true;
	}

	/**
	 * Get a room found by it name
	 * @param room : The searched room's name
	 * @return Room : The found room
	 * @throws UnfoundRoomException : Raised when room is not found
	 */
	public Room getRoomByName(String room) throws UnfoundRoomException {
		return json.readRooms().stream()
			.filter(r -> room.equals(r.getName())).findFirst()
			.orElseThrow(() -> new UnfoundRoomException(room));
	}

	/**
	 * Check if a room is open at a given time
	 * @param room : The room
	 * @param start : The time at which we want to know if the room is booked
	 * @return boolean : true if the room is booked, false if not
	 */
	public boolean isBooked(Room room, long start) {
		if(!room.getBookList().isEmpty()) {
			List<Book> booksInInterval = bookCtrl.getBooksInInterval(room, start, start + 1);
			if(!booksInInterval.isEmpty()) return true;
		}
		return false;
	}
}
