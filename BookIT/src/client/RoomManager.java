package client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class RoomManager {
	
	public RoomManager() {}

	public List<Room> look(Map<String, String> commandMap) throws ParseException {
		if("a".equals(commandMap.get("options"))) return getAllRooms();

		boolean closed = commandMap.get("options") != null && commandMap.get("options").contains("f");
		boolean itroom = commandMap.get("options") != null && commandMap.get("options").contains("i");
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		Date date = commandMap.get("date") == null ? new Date() : df.parse(commandMap.get("date"));
		int duree = commandMap.get("duree") == null ? 0 : Integer.parseInt(commandMap.get("duree"));
		int nbPlaces = commandMap.get("nbplaces") == null ? 0 : Integer.parseInt(commandMap.get("nbplaces"));
		
		List<Room> rooms = getAllRooms(date.getTime(), duree, closed, nbPlaces);
		if(itroom) return rooms.stream().filter(r -> r.isItroom()).collect(Collectors.toList());
		return rooms;
	}
	
	public Room see(Map<String, String> commandMap) {
		return getRoom(commandMap.get("room"));
	}

	private List<Room> getAllRooms(long date, int duree, boolean closed, int nbPlaces) {
		List<Room> rooms = getAllRooms();
		List<Room> roomsWithOptions = new ArrayList<>();
		for(Room r : rooms) {
			if(isAvailable(r, date, duree, closed) && nbPlaces <= r.getCapacity()) roomsWithOptions.add(r);
		}
		return roomsWithOptions;
	}

	private List<Room> getAllRooms()  {
		Gson gson = new Gson();
		Type listRooms = new TypeToken<List<Room>>(){}.getType();
		try {
			return gson.fromJson(new FileReader("../testJSON.json"), listRooms);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}	
	}

	private Room getRoom(String room) {
		List<Room> rooms = getAllRooms();
		for(Room r : rooms) {
			if(room.equals(r.getName())) return r;
		}
		return new Room();
	}

	public boolean isAvailable(Room r, long date, int duree, boolean closed) {
		long dateOffset = date + (duree*60000);
		if(r.getBookList() != null) {
			for(Book b : r.getBookList()) {
				if((!b.isAccessible() || closed) && (
						(date >= b.getStartDate() && date < b.getEndDate())
						|| (b.getStartDate() >= date && b.getStartDate() < dateOffset))) return false;
			}
		}
		return true;
	}

	public boolean isOpened(Room r, long time) {
		if(r.getBookList() != null) {
			for(Book b : r.getBookList()) {
				if(time >= b.getStartDate() && time < b.getEndDate()) return true;
			}
		}
		return false;
	}

	public String book(Map<String, String> commandMap) { return "book"; }

	
}
