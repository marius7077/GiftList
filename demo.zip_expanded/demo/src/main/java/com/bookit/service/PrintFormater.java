package com.bookit.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.bookit.model.Book;
import com.bookit.model.Room;

@Service
public class PrintFormater {

	public String buildDisplayRoomLook(Room r) {
		/*String displayRoom = r.getName();
		displayRoom += "(" + (rm.isAvailable(r, System.currentTimeMillis(), 0, false) ? (rm.isOpened(r, System.currentTimeMillis()) ? "ouverte" : "libre") : "occup�e") + ") - " ;
		displayRoom += (r.isItroom() ? "salle informatique" : "salle de cours") + " - " + r.getL10n() + " : ";
		displayRoom += "Nombre de places : " + r.getCapacity();
		
		return displayRoom;*/
		return "TO DO";
	}

	public String buildDisplayRoomSee(Room room) {
		/*String displayRoom = "Salle " + room.getName();
		displayRoom += "\nSalle informatique : " + (room.isItroom() ? "Oui" : "Non");
		displayRoom += "\nLocalisation : " + room.getL10n();
		displayRoom += "\nÉtat actuel : " + (rm.isAvailable(room, System.currentTimeMillis(), 0, false) ? (rm.isOpened(room, System.currentTimeMillis()) ? "Ouverte" : "Libre") : "Occup�e");
		displayRoom += "\nNombre de place : " + room.getCapacity();
		
		return displayRoom;*/
		return "TO DO";
	}

	public String buildDisplayBookSee(Book b) {
		/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		String displayBook = df.format(b.getStartDate()) + " - " + df.format(b.getEndDate());
		displayBook += " : " + (b.isAccessible() ? "ouverte" : "occup�e");
		displayBook += " - " + b.getDescription() + " (" + b.getOwner() + ")";

		return displayBook;*/
		return "To dO";
	}
}