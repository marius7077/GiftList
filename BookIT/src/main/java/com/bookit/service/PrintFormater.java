package com.bookit.service;

import com.bookit.controller.Command;
import com.bookit.controller.RoomController;
import com.bookit.model.Book;
import com.bookit.model.Room;
import java.text.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrintFormater {
	
	@Autowired
	private DateFormat df;

	@Autowired
	private RoomController roomCtrl;

	/**
	 * Get the string representing a room for the list command
	 * @param room : The room to display
	 * @param command : The command to compute availability by date
	 * @return String : The string to print
	 */
	public String buildDisplayRoomLook(Room room, Command command) {
		String displayRoom = room.getName();
		if(room.getL10n() != null) {
			displayRoom += ", " + room.getL10n().toLowerCase();			
		}
		displayRoom += " (" + room.getCapacity() + " places) - ";
		displayRoom += (room.isItroom() ? "salle informatique" : "salle de cours");
		displayRoom += " : " + getState(room, command);

		return displayRoom;
	}

	/**
	 * Get the string representing a room for the describe command
	 * @param room : The room to display
	 * @return String : The string to print
	 */
	public String buildDisplayRoomSee(Room room) {
		String displayRoom = "Salle " + room.getName();
		displayRoom += "\nSalle informatique : " + (room.isItroom() ? "Oui" : "Non");
		displayRoom += "\nLocalisation : " + toFormatCase(room.getL10n());
		displayRoom += "\nÉtat actuel : " + toFormatCase(getState(room, new Command()));
		displayRoom += "\nNombre de places : " + room.getCapacity();
		
		return displayRoom;
	}

	/**
	 * Get the string representing a book for the describe command
	 * @param b : The book to display
	 * @return String : The string to print
	 */
	public String buildDisplayBook(Book b) {
		String displayBook = df.format(b.getStartDate()) + " - " + df.format(b.getEndDate());
		displayBook += " : " + (b.isPriv() ? "privé" : "public");
		displayBook += " - " + b.getDescription() + " (" + b.getOwner() + ")";

		return displayBook;
	}

	/**
	 * Get the state of a room at the time
	 * @param r : The room concerned
	 * @param command : The command to compute state by time
	 * @return String : The string representing the state
	 */
	protected String getState(Room r, Command command) {
		long start = command.getStartDate();
		long end = command.getEndDate();
		boolean available = roomCtrl.isAccessible(r, start, end, false);
		boolean opened = roomCtrl.isBooked(r, start);
		if(available) {
				if(opened) return "ouverte";
				else return "libre";
		}
		else return "occupée";
	}

	/**
	 * Return a string formated like this : string -> String
	 * @param s : The string to format
	 * @return String : The formated string
	 */
	protected String toFormatCase(String s) {
		return (s.charAt(0) + "").toUpperCase() + s.substring(1);
	}
}