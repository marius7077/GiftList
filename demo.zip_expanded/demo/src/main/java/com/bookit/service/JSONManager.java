package com.bookit.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bookit.model.Room;
import com.bookit.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

@Service
public class JSONManager {
	private final static String urlRooms = "src/main/resources/rooms.json";
	private final static String urlUsers = "src/main/resources/users.json";
	
	@Autowired
	private Gson gson;
	@Autowired
	@Qualifier("listroom")
	private Type listroomtype;
	@Autowired
	@Qualifier("listuser")
	private Type listusertype;
	
	/**
	 * Get the list of all rooms contained in a json file
	 * @return List<Room> : The list
	 */
	public List<Room> readRooms() {
		try {
			return gson.fromJson(new FileReader(urlRooms), listroomtype);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * Rewrite the json's room list
	 * @return List<Room> : The list to write
	 */
	public boolean writeRooms(List<Room> rooms) {
		JsonElement jtree = gson.toJsonTree(rooms, listroomtype);
		FileWriter writer;
		try {
			writer = new FileWriter(urlRooms);
			writer.write(jtree.toString());
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}