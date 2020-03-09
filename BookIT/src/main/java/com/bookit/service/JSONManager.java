package com.bookit.service;

import com.bookit.model.Room;
import com.bookit.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JSONManager {

	@Value("${urlRooms}")
	private String urlRooms;
	@Value("${urlUsers}")
	private String urlUsers;
	@Value("${errorReadRooms}")
	private String errorReadRooms;
	@Value("${errorReadUsers}")
	private String errorReadUsers;
	@Value("${errorWriteRooms}")
	private String errorWriteRooms;
	
	@Autowired
	private Gson gson;
	@Autowired
	@Qualifier("listroom")
	private Type listroomtype;
	@Autowired
	@Qualifier("listuser")
	private Type listusertype;

	Logger logger = Logger.getLogger(JSONManager.class.getName());
	
	/**
	 * Get the list of all rooms contained in a json file
	 * @return List<Room> : The list
	 */
	public List<Room> readRooms() {
		try {
			return gson.fromJson(new FileReader(urlRooms), listroomtype);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			logger.log(Level.WARNING, errorReadRooms);
			return new ArrayList<>();
		}
	}
	
	/**
	 * Get the list of all users contained in a json file
	 * @return List<Room> : The list
	 */
	public List<User> readUsers() {
		try {
			return gson.fromJson(new FileReader(urlUsers), listusertype);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			logger.log(Level.WARNING, errorReadUsers);
			return new ArrayList<>();
		}
	}

	/**
	 * Rewrite the json's room list
	 * @return List<Room> : The list to write
	 */
	public boolean writeRooms(List<Room> rooms) {
		JsonElement jtree = gson.toJsonTree(rooms, listroomtype);
		try (FileWriter writer = new FileWriter(urlRooms)){
			writer.write(jtree.toString());
			return true;
		} catch (IOException e) {
			logger.log(Level.WARNING, errorWriteRooms);
			return false;
		}
	}
}