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
	ArgumentParser ap;

	public AppConsole(InputStream input, RoomManager rm) {
		this.sc = new Scanner(input);
		this.rm = new RoomManager();
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
			case "l": 
				List<Room> rooms = rm.look(commandMap);
				rooms.stream().forEach(r -> System.out.println(buildDisplayRoomLook(r)));
				break;
			case "s": 
				Room room = rm.see(commandMap);
				System.out.println(buildDisplayRoomSee(room));
				List<Book> books = getBooksToDisplay(room, commandMap.get("options"), commandMap.get("date"));
				books.stream().forEach(b -> System.out.println(buildDisplayBookSee(b)));
				break;
			case "b": System.out.println(rm.book(commandMap)); break;
			default: System.out.println(this.help());
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
		displayRoom += "\n�tat actuel : " + (rm.isAvailable(room, System.currentTimeMillis(), 0, false) ? (rm.isOpened(room, System.currentTimeMillis()) ? "Ouverte" : "Libre") : "Occup�e");
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