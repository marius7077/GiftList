package com.bookit.controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.bookit.exception.CommandException;
import com.bookit.exception.ExceptionManager;
import com.bookit.exception.UnfoundRoomException;
import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.model.User;
import com.bookit.service.ArgumentParser;
import com.bookit.service.PrintFormater;

@Controller
public class ConsoleApplication {
	
	private static final String CANCEL_BOOK = "Réservation annulée.";
	private static final String IMPOSSIBLE_BOOK = 
		"Réservation impossible. "
		+ "Consultez les disponibilités de la salle.";
	private static final String ENTER_LOGIN = "Entrez votre login : ";
	private static final String ENTER_PASSWORD = "Entrez votre mot de passe : ";
	private static final String AUTHENT_FAILED = "Identifiants incorrects !";
	private static final String LOCKED_ACCOUNT = 
		"Votre compte est verrouillé. Veuillez contacter votre administration.";
	private static final String AUTHENT_SUCCES = "Connecté en tant que ";
	private static final String RETRY = "Réessayez ou appuyez sur 'Entrer' pour quitter.";
	private static final String ENTER_DESC = 
		"Entrez une description pour la réservation ou tapez 'Entrer' pour quitter : ";
	private static final String HELP = 
		"Les commandes disponibles sont les suivantes ;\n"
		+ "- list : affiche une liste de salle,\n"
		+ "- describe : affiche les détails d'une salle,\n"
		+ "- book : permet de réserver une salle.\n"
		+ "\nTapez help <fonction> pour plus d'informations.";
	private static final String HELP_DESCRIBE = 
		"La fonction describe affiche les détails d'une salle.\n\n"
		+ "-a (all) permet d'afficher l'historique de réservation de la salle\n"
		+ "-d (date) permet de spécifier un intervale de dates pour l'affichage des réservations.\n"
		+ "\t → L'intervale de dates doit être au format AAAA-MM-JJ_hh:mm;AAAA-MM-JJ_hh:mm\n"
		+ "Par défaut, les réservations en cours seront affichées.\n\n"
		+ "Exemple d'utilisation : describe <nom_de_salle> [-(a|d <intervalle_de_date>)]\n";
	private static final String HELP_LIST = 
		"La fonction describe une liste de salle.\n\n"
		+ "-a (all) permet d'afficher l'intégralité des salles\n"
		+ "-c (closed) permet d'afficher les salles inoccupées\n"
		+ "-i (it) permet d'afficher les salles informatiques\n"
		+ "-d (date) permet de spécifier un intervale de dates pour l'affichage des salles.\n"
		+ "\t → L'intervale de dates doit être au format AAAA-MM-JJ_hh:mm;AAAA-MM-JJ_hh:mm\n"
		+ "-n (number) permet d'afficher les salles ayant un minimum de place\n"
		+ "\t → La capacité minimale renseignée doit être un nombre entier\n"
		+ "Les options c, i, d et n peuvent être combinées.\n"
		+ "Par défaut, les salles actuellement accessibles seront affichées.\n\n"
		+ "Exemple d'utilisation : list [-(a|c|i|d|n) <intervalle_de_date> <capacité_minimale>]\n"
		+ "\t → Attention, les dates doivent être spécifiées avant la capacité !\n";

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

	/**
	 * While application running, parse and execute the user's entries
	 * @throws CommandException when command is not recognized
	 */
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
				} catch (UnfoundRoomException e) {
					exceptionCtrl.exceptionCtrl(e);
				}
			}
		}
	}
	
	/**
	 * After a user's entry is parsed into a command object, execute this command
	 * @param command : An object which contains all information about the user's entry
	 * @param scanner : A scanner to read an other user entry if necessary
	 * @throws UnfoundRoomException  : Exception raised if the command's room doesn't exists
	 */
	private void execute(Command command, Scanner scanner) {
		switch (command.getMethod()) {
			case "list":
				List<Room> rooms;
				rooms = roomCtrl.list(command);
				rooms.stream().forEach(r -> printer.println(formater.buildDisplayRoomLook(r)));
				break;
		
			case "describe":
				Room room = command.getRoom();
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
							Book book = new Book(
								command.getStartDate(), 
								command.getEndDate(), 
								user.getLogin(), 
								description, 
								command.getPrivateOption()
							);
							roomCtrl.book(command.getRoom(), book);
							printer.println(formater.buildDisplayBook(book));
						} else printer.println(CANCEL_BOOK);
					} else printer.println(CANCEL_BOOK);
				} else printer.println(IMPOSSIBLE_BOOK);
				break;
				
			case "help":
				if(!"".equals(command.getHelp())) printer.print(help(command.getHelp()));
				else printer.print(help());
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
		String login, password;
		User user = null;
		
		while(user == null) {
			printer.print(ENTER_LOGIN);
			login = scanner.nextLine();
			if("".equals(login)) return null;
			printer.print(ENTER_PASSWORD);
			password = scanner.nextLine();
			user = userCtrl.connect(login, password);

			if(user == null) printer.println(AUTHENT_FAILED);
			else if(!user.isAuthorized()) printer.println(LOCKED_ACCOUNT);
			else {
				printer.println(AUTHENT_SUCCES + user.getName());
				return user;
			}

			printer.println(RETRY);
		}
		
		return user;
	}

	/**
	 * Ask to the user about the booking he's creating's description
	 * @param scanner : A scanner to read user entry
	 * @return String : The user entry
	 */
	private String buildDescription(Scanner scanner) {
		printer.print(ENTER_DESC);
		return scanner.nextLine();
	}
	
	/**
	 * Get an help message describing the commands available
	 * @return String : The help message
	 */
	private String help() {
		return HELP;
	}
	
	/**
	 * Get an help message describing a specific command
	 * @param method : The specific method to describe
	 * @return String : The help message
	 */
	private String help(String method) {
		switch(method) {
			case "list":
			return HELP_LIST;
			case "describe":
			return HELP_DESCRIBE;
			default:
				return help();
		}
	}
}
