package com.bookit.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;

import com.bookit.model.Book;
import com.bookit.model.Room;

@Controller
public class BookController {

	public List<Book> getBooksToDisplay(Room room, String a, String d) {
		/*if(a == "a") return room.getBookList().stream().sorted(Comparator.comparingLong(Book::getStartDate)).collect(Collectors.toList());
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
		return bookList;*/
		return null;
	}

	public Book tryBooking(Map<String, String> commandMap) {
		/*if(connectedUser != null && !connectedUser.isLocked()) {
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
		return null;*/return null;
	}
	
	/*case "connect":
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
	break;*/

}
