package com.bookit.controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.bookit.exception.CommandException;
import com.bookit.exception.ExceptionManager;
import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.service.ArgumentParser;
import com.bookit.service.PrintFormater;

@Controller
public class ConsoleApplication {
	
	@Autowired
	private InputStream reader;
	@Autowired
	@Qualifier("printer")
	private PrintStream printer;
	@Autowired
	private ArgumentParser parser;
	@Autowired
	private PrintFormater formater;
	@Autowired
	private RoomController roomCtrl;
	@Autowired
	private BookController bookCtrl;
	@Autowired
	private ExceptionManager exceptionCtrl;

	public void start() {
		String line;
		Scanner sc = new Scanner(reader);
		while(true) {
			printer.print("> ");	
			line = sc.nextLine();
			if(!"".equals(line)) {
				try {
					Map<String, String> commandMap = parser.parseArgs(line);
					this.execute(commandMap);
				} catch (CommandException e) {
					exceptionCtrl.exceptionCtrl(e);
				}
			}
		}
	}

	private void execute(Map<String, String> commandMap) throws CommandException {
		switch(commandMap.get("method")) {
			case "list":
				List<Room> rooms = roomCtrl.look(commandMap);
				rooms.stream().forEach(r -> printer.println(formater.buildDisplayRoomLook(r)));
				break;
			case "describe": 
				Room room = roomCtrl.see(commandMap);
				printer.println(formater.buildDisplayRoomSee(room));
				List<Book> books = bookCtrl.getBooksToDisplay(room, commandMap.get("options"), commandMap.get("date"));
				books.stream().forEach(b -> printer.println(formater.buildDisplayBookSee(b)));
				break;
			case "book": 
				Book book = bookCtrl.tryBooking(commandMap); 
				printer.println(formater.buildDisplayBookSee(book));
				break;
			default: throw new CommandException();
		}
	}
}
