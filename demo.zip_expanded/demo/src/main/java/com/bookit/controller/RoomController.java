package com.bookit.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.bookit.model.Room;

@Controller
public class RoomController {

	@Autowired
	@Qualifier("date10")
	private DateFormat df10;
	
	public List<Room> look(Map<String, String> commandMap) {
		if("a".equals(commandMap.get("options"))) return getAllRooms();

		boolean closed = commandMap.get("options") != null && commandMap.get("options").contains("f");
		boolean itroom = commandMap.get("options") != null && commandMap.get("options").contains("i");
		
		Date date = commandMap.get("date") == null ? new Date() : df10.parse(commandMap.get("date"));
		int duree = commandMap.get("duree") == null ? 0 : Integer.parseInt(commandMap.get("duree"));
		int nbPlaces = commandMap.get("nbplaces") == null ? 0 : Integer.parseInt(commandMap.get("nbplaces"));
		
		List<Room> rooms = getAllRoomsByOptions(date.getTime(), duree, closed, nbPlaces);
		if(itroom) return rooms.stream().filter(r -> r.isItroom()).collect(Collectors.toList());
		return rooms;
	}

	public Room see(Map<String, String> commandMap) {
		//return getRoom(commandMap.get("room"));
		return null;
	}

	private List<Room> getAllRooms()  {
		/*Gson gson = new Gson();
		Type listRooms = new TypeToken<List<Room>>(){}.getType();
		try {
			return gson.fromJson(new FileReader("../rooms.json"), listRooms);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}*/
		return null;
	}
	
	private List<Room> getAllRoomsByOptions(long date, int duree, boolean closed, int nbPlaces) {
		/*List<Room> rooms = getAllRooms();
		List<Room> roomsWithOptions = new ArrayList<>();
		for(Room r : rooms) {
			if(isAvailable(r, date, duree, closed) && nbPlaces <= r.getCapacity()) roomsWithOptions.add(r);
		}
		return roomsWithOptions;*/
		return null;
	}
}
