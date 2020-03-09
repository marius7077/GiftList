package com.bookit.controller;

import com.bookit.model.Book;
import com.bookit.model.Room;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {

	/**
	 * Get a room's book list to display according to the command options
	 * @param room : The room of which we want display some books
	 * @param command : The command with all information about which books have to be displayed
	 * @return List<Book> : The computed list of books
	 */
	public List<Book> getBooksToDisplay(Room room, Command command) {
		if(command.getAllOption()) return room.getBookList();
		
		return getBooksInInterval(room, command.getStartDate(), command.getEndDate());
	}

	/**
	 * Get a list containing room's book which are in the command's interval of time
	 * @param room : The room of which we want display some books
	 * @param start : The start of the interval in millisecond
	 * @param end : The end of the interval in millisecond
	 * @return List<Book> : The computed list of books
	 */
	public List<Book> getBooksInInterval(Room room, long start, long end) {
		return room.getBookList().stream()
				.filter(b -> inInterval(b,  start, end))
				.collect(Collectors.toList());
	}

	/**
	 * Check if a time intersection exists between a book and an interval of time
	 * @param book : The book we want to know if it shares time with an interval
	 * @param startDate : The start of the interval of time, in millisecond
	 * @param endDate : The end of the interval of time, in millisecond
	 * @return boolean : True if an intersection exists, false if not
	 */
	public boolean inInterval(Book book, Long startDate, Long endDate) {
		if(book.getStartDate() >= startDate && book.getStartDate() < endDate) {
			return true;
		}
    return startDate >= book.getStartDate() && startDate < book.getEndDate();
  }
}
