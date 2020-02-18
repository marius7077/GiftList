package com.bookit.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;

import com.bookit.model.Book;
import com.bookit.model.Room;

@Controller
public class BookController {

	public List<Book> getBooksToDisplay(Room room, Command command) {
		if(command.getAllOption()) return room.getBookList();
		return room.getBookList().stream().filter(b -> inInterval(b,  command.getStartDate(), command.getEndDate())).collect(Collectors.toList());
	}

	public boolean inInterval(Book book, Long startDate, Long endDate) {
		if(book.getStartDate() >= startDate && book.getStartDate() < endDate) return true;
		if(startDate >= book.getStartDate() && startDate < book.getEndDate()) return true;
		return false;
	}
}
