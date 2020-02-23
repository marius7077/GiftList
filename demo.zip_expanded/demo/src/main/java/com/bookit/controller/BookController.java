package com.bookit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;

import com.bookit.model.Book;
import com.bookit.model.Room;

@Controller
public class BookController {

	/**
	 * Get a room's book list to display according to the command options
	 * @param room : The room of which we want display some books
	 * @param command : The command with all information about which books have to be displayed
	 * @see Command
	 * @return A list of books
	 */
	public List<Book> getBooksToDisplay(Room room, Command command) {
		if(command.getAllOption()) return room.getBookList();
		
		//Return the list of the rooms's book which are in the command's interval of time
		return room.getBookList().stream()
				.filter(b -> inInterval(b,  command.getStartDate(), command.getEndDate()))
				.collect(Collectors.toList());
	}

	/**
	 * Check if a time intersection exists between a book and an interval of time
	 * @param book : The book we want to know if it shares time with an interval
	 * @param startDate : The start of the interval of time, in millisecond
	 * @param endDate : The end of the interval of time, in millisecond
	 * @return True if an intersection exists, false if not
	 */
	public boolean inInterval(Book book, Long startDate, Long endDate) {
		if(book.getStartDate() >= startDate && book.getStartDate() < endDate) return true;
		if(startDate >= book.getStartDate() && startDate < book.getEndDate()) return true;
		return false;
	}
}
