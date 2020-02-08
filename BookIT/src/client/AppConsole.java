package client;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AppConsole {
	Scanner sc;
	RoomManager rm;
	UserManager um;
	ArgumentParser ap;
	User connectedUser;

	public AppConsole(InputStream input) {
		this.sc = new Scanner(input);
		this.rm = new RoomManager();
		this.um = new UserManager();
		this.ap = new ArgumentParser();
	}

	public void start() throws ParseException {
		String line;
		while(true) {
			System.out.print("> ");	
			line = sc.nextLine();
			if(!"".equals(line)) {
				Map<String, String> commandMap = ap.parseArgs(line);
				this.execute(commandMap);
			}
		}
	}
	
	private void execute(Map<String, String> commandMap) throws ParseException {
		switch(commandMap.get("method")) {
			case "look": 
				List<Room> rooms = rm.look(commandMap);
				rooms.stream().forEach(r -> System.out.println(buildDisplayRoomLook(r)));
				break;
			case "see": 
				Room room = rm.see(commandMap);
				System.out.println(buildDisplayRoomSee(room));
				List<Book> books = getBooksToDisplay(room, commandMap.get("options"), commandMap.get("date"));
				books.stream().forEach(b -> System.out.println(buildDisplayBookSee(b)));
				break;
			case "book": 
				if(connectedUser != null) {
					Book book = tryBooking(commandMap); 
					System.out.println(buildDisplayBookSee(book));
				} else System.out.println("Vous n'êtes pas connecté.");
				break;		
			case "connect":
				if(connectedUser == null) {
					System.out.println("Connexion : appuyez sur entrer sans écrire de login pour annuler.");
					connectedUser = tryConnexion(); 
				} else System.out.println("Vous êtes déjà connecté. Déconnectez-vous pour vous connecter à un autre compte.");
				break;
			case "disconnect":
				if(connectedUser != null) {
					connectedUser = null;
					System.out.println("Vous êtes déconnecté.");
				} else System.out.println("Vous n'êtes pas connecté.");
				break;
			default: System.out.println(this.help());
		}
	}

	private Book tryBooking(Map<String, String> commandMap) {
		if(connectedUser != null && !connectedUser.isLocked()) {
			Room room = rm.getRoom(commandMap.get("room"));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
			long date = System.currentTimeMillis();
			try {
				date = df.parse(commandMap.get("date")).getTime();
			} catch (ParseException e) { e.printStackTrace(); }
			int duree = Integer.parseInt(commandMap.get("duree"));
			boolean closed = "l".equals(commandMap.get("options"));
			if(room.getName().equals(commandMap.get("room")) && rm.isAvailable(room, date, duree, closed)) {
				String descri = "";
				while("".equals(descri)) {
					System.out.print("Description > ");
					descri = sc.nextLine();
				}
				Book b = rm.book(room.getName(), date, duree, descri, connectedUser.getLogin(), closed);
				return b;
			}
		}
		return null;
	}

	private User tryConnexion() {
		User user = null;
		String login, pwd;
		
		System.out.print("Login > ");
		while(true) {
			login = sc.nextLine();
			if("".equals(login)) return null;
			
			System.out.print("Mot de passe > ");
			pwd = sc.nextLine();
			
			user = um.connect(login, pwd);
			if(user == null) {
				System.out.print("Échec de connexion. Réessayer ? Login > ");
			} else if (user.isLocked()) {
				System.out.println("Votre compte est verrouillé. Veuillez contacter l'administration.");
				return null;
			}
			else {
				System.out.println("Connecté en tant que " + user.getName());
				return user;
			}
		}
	}

	private String buildDisplayRoomLook(Room r) {
		String displayRoom = r.getName();
		displayRoom += "(" + (rm.isAvailable(r, System.currentTimeMillis(), 0, false) ? (rm.isOpened(r, System.currentTimeMillis()) ? "ouverte" : "libre") : "occup�e") + ") - " ;
		displayRoom += (r.isItroom() ? "salle informatique" : "salle de cours") + " - " + r.getL10n() + " : ";
		displayRoom += "Nombre de places : " + r.getCapacity();
		
		return displayRoom;
	}
		
	private String buildDisplayRoomSee(Room room) {
		String displayRoom = "Salle " + room.getName();
		displayRoom += "\nSalle informatique : " + (room.isItroom() ? "Oui" : "Non");
		displayRoom += "\nLocalisation : " + room.getL10n();
		displayRoom += "\nÉtat actuel : " + (rm.isAvailable(room, System.currentTimeMillis(), 0, false) ? (rm.isOpened(room, System.currentTimeMillis()) ? "Ouverte" : "Libre") : "Occup�e");
		displayRoom += "\nNombre de place : " + room.getCapacity();
		
		return displayRoom;
	}
	
	private String buildDisplayBookSee(Book b) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		String displayBook = df.format(b.getStartDate()) + " - " + df.format(b.getEndDate());
		displayBook += " : " + (b.isAccessible() ? "ouverte" : "occup�e");
		displayBook += " - " + b.getDescription() + " (" + b.getOwner() + ")";

		return displayBook;
	}

	private List<Book> getBooksToDisplay(Room room, String a, String d) {
		if(a == "a") return room.getBookList().stream().sorted(Comparator.comparingLong(Book::getStartDate)).collect(Collectors.toList());
		long time, startTime, endTime;
		time = System.currentTimeMillis();
		startTime = getMidnightTime(time, false);
		endTime = getMidnightTime(time, true);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if(d != null) {
			String[] dates = d.split(":");
			try {
				startTime = getMidnightTime((df.parse(dates[0])).getTime(), false);
				if(dates.length == 2) endTime = getMidnightTime((df.parse(dates[1])).getTime(), true);
				else endTime = getMidnightTime((df.parse(dates[0])).getTime(), true);
			} catch (ParseException e) {e.printStackTrace();}
		}
		
		List<Book> bookList = new ArrayList<>();
		for(Book b : room.getBookList().stream().sorted(Comparator.comparingLong(Book::getStartDate)).collect(Collectors.toList())) {
			if(startTime >= b.getStartDate() && startTime < b.getEndDate() || b.getStartDate() >= startTime && b.getStartDate() < endTime) {
				bookList.add(b);
			}
		}
		return bookList;
	}
	
	private long getMidnightTime(long time, boolean endOfDay) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if(endOfDay) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return cal.getTimeInMillis();
	}

	public String help() {return "str_help";}
}