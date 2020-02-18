package com.bookit.controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.bookit.exception.CommandException;
import com.bookit.exception.ExceptionManager;
import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.model.User;
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
	private UserController userCtrl;
	@Autowired
	private ExceptionManager exceptionCtrl;

	public void start() {
		String line;
		Scanner scanner = new Scanner(reader);
		
		while (true) {
			printer.print("> ");
			line = scanner.nextLine();
			if (!"".equals(line)) {
				try {
					Command command = parser.parseArgs(line);
					this.execute(command, scanner);
				} catch (CommandException e) {
					exceptionCtrl.exceptionCtrl(e);
				}
			}
		}
	}

	private void execute(Command command, Scanner scanner) throws CommandException {
		switch (command.getMethod()) {
			case "list":
				List<Room> rooms;
				try {
					rooms = roomCtrl.list(command); 
					rooms.stream().forEach(r -> printer.println(formater.buildDisplayRoomLook(r)));} 
				catch (ParseException e) { throw new CommandException(command.getMethod()); }
				break;
		
			case "describe":
				Room room = roomCtrl.describe(command);
				printer.println(formater.buildDisplayRoomSee(room));
				List<Book> books = bookCtrl.getBooksToDisplay(room, command);
				books.stream().forEach(b -> printer.println(formater.buildDisplayBook(b)));
				break;
				
			case "book":
				if(roomCtrl.checkBookability(command)) {
					User user = connect(scanner);
					if(user != null) {
						String description = buildDescription(scanner);
						if(!"".equals(description)) {
							Book book = new Book(command.getStartDate(), command.getEndDate(), user.getLogin(), description, command.getPrivateOption());
							roomCtrl.book(command.getRoom(), book);
							printer.println(formater.buildDisplayBook(book));
						} else printer.println("Réservation annulée.");
					} else printer.println("Réservation annulée.");
				} else printer.println("Réservation impossible. Consultez les disponibilités de la salle.");
				break;
				
				default:
					throw new CommandException();
		}
	}

	private User connect(Scanner scanner) {
		String login, password;
		User user = null;
		
		while(user == null) {
			printer.print("Entrez votre login : ");
			login = scanner.nextLine();
			if("".equals(login)) return null;
			printer.print("Entrez votre mot de passe : ");
			password = scanner.nextLine();
			user = userCtrl.connect(login, password);

			if(user == null) printer.println("Identifiants incorrects !");
			else if(!user.isAuthorized()) printer.println("Votre compte est verrouillé. Veuillez contacter votre administration.");
			else {
				printer.println("Connecté en tant que " + user.getName());
				return user;
			}

			printer.println("Réessayez ou appuyez sur 'Entrer' pour quitter.");
		}
		
		return user;
	}

	private String buildDescription(Scanner scanner) {
		printer.print("Entrez une description pour la réservation ou tapez 'Entrer' pour quitter : ");
		return scanner.nextLine();
	}
}
