package com.bookit.controller;

import com.bookit.exception.CommandException;
import com.bookit.exception.ExceptionManager;
import com.bookit.exception.UnfoundRoomException;
import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.model.User;
import com.bookit.service.ArgumentParser;
import com.bookit.service.PrintFormater;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class ConsoleApplication {
	@Value("${cancelBook}")
	private String cancelBook;
	@Value("${impossibleBook}")
	private String impossibleBook;
	@Value("${enterLogin}")
	private String enterLogin;
	@Value("${enterPassword}")
	private String enterPassword;
	@Value("${authentFailed}")
	private String authentFailed;
	@Value("${lockedAccount}")
	private String lockedAccount;
	@Value("${authentSucces}")
	private String authentSucces;
	@Value("${retry}")
	private String retry;
	@Value("${enterDesc}")
	private String enterDesc;
	@Value("${consoleAppplicationHelp}")
	private String help;
	@Value("${helpDescribe}")
	private String helpDescribe;
	@Value("${helpList}")
	private String helpList;
	@Value("${helpBook}")
	private String helpBook;

	@Autowired
	private InputStream reader;
	@Autowired
	private Logger logger;
	@Autowired
	private ArgumentParser parser;
	@Autowired
	private PrintFormater formater;
	@Autowired
	private RoomController roomCtrl;
	@Autowired
	private BookController bookCtrl;
	@Autowired
	private UserController userCtrl;
	@Autowired
	private ExceptionManager exceptionCtrl;

	/**
	 * While application running, parse and execute the user's entries
	 * @throws CommandException when command is not recognized
	 */
	public void start() {
		String line;
		Scanner scanner = new Scanner(reader);

		do {
			logger.log(Level.INFO, "> ");
			line = scanner.nextLine();
			if (!"".equals(line)) {
				try {
					Command command = parser.parseArgs(line);
					this.execute(command, scanner);
				} catch (CommandException | UnfoundRoomException e) {
					exceptionCtrl.exceptionCtrl(e);
				}
			}
		} while (true);
	}
	
	/**
	 * After a user's entry is parsed into a command object, execute this command
	 * @param scanner : A scanner to read an other user entry if necessary
	 * @throws UnfoundRoomException  : Exception raised if the command's room doesn't exists
	 */
	public void execute(Command command, Scanner scanner) {

		switch (command.getMethod()) {
			case "list":
				List<Room> rooms;
				rooms = roomCtrl.list(command);
				rooms.stream().forEach(r -> {
					String display = formater.buildDisplayRoomLook(r, command);
					logger.log(Level.INFO, display);
				});
				break;
		
			case "describe":
				Room room = command.getRoom();
				String display = formater.buildDisplayRoomSee(room);
				logger.log(Level.INFO, display);
				List<Book> books = bookCtrl.getBooksToDisplay(room, command);
				//Sort and display books
				books.stream().sorted(Comparator.comparing(Book::getStartDate))
					.forEach(b -> {
						String display2 = formater.buildDisplayBook(b);
						logger.log(Level.INFO, display2);
					});
				break;
				
			case "book":
				if(roomCtrl.checkBookability(command)) {
					User user = connect(scanner);
					if(user != null) {
						String description = buildDescription(scanner);
						if(!"".equals(description)) {
							Book book = new Book(
								command.getStartDate(), 
								command.getEndDate(), 
								user.getName(), 
								description, 
								command.getPrivateOption()
							);
							roomCtrl.book(command.getRoom(), book);
							String display3 = formater.buildDisplayBook(book);
							logger.log(Level.INFO, display3);
						} else logger.log(Level.INFO, cancelBook);
					} else logger.log(Level.INFO, cancelBook);
				} else logger.log(Level.INFO, impossibleBook);
				break;
				
			case "help":
				if(!"".equals(command.getHelp())) {
					String display4 = help(command.getHelp());
					logger.log(Level.INFO, display4);
				}
				else {
					String display5 = help();
					logger.log(Level.INFO, display5);
				}
				break;				
				
			default:
				break;
		}
	}

	/**
	 * Ask to the user about his login and password to connect him before booking
	 * @param scanner : A scanner to read user entry
	 * @return User : The User Object, or null if connection was canceled
	 */
	private User connect(Scanner scanner) {
		String login;
		String password;
		User user;
		
		while(true) {
			logger.log(Level.INFO, enterLogin);
			login = scanner.nextLine();
			if("".equals(login)) return null;
			logger.log(Level.INFO, enterPassword);
			password = scanner.nextLine();
			user = userCtrl.connect(login, password);

			if(user == null) logger.log(Level.INFO, authentFailed);
			else if(!user.isAuthorized()) {
				logger.log(Level.INFO, lockedAccount);
			}
			else {
				User finalUser = user;
				logger.log(Level.INFO,() -> authentSucces + finalUser.getName());
				return user;
			}
			logger.log(Level.INFO, retry);
		}
	}

	/**
	 * Ask to the user about the booking he's creating's description
	 * @param scanner : A scanner to read user entry
	 * @return String : The user entry
	 */
	private String buildDescription(Scanner scanner) {
		logger.log(Level.INFO, enterDesc);
		return scanner.nextLine();
	}
	
	/**
	 * Get an help message describing the commands available
	 * @return String : The help message
	 */
	private String help() {
		return help;
	}
	
	/**
	 * Get an help message describing a specific command
	 * @param method : The specific method to describe
	 * @return String : The help message
	 */
	private String help(String method) {
		switch(method) {
			case "list":
			return helpList;
			case "describe":
			return helpDescribe;
			case "book":
				return helpBook;
			default:
				return help();
		}
	}
}
