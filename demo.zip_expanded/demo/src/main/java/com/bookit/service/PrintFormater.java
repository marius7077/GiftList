package com.bookit.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookit.controller.RoomController;
import com.bookit.model.Book;
import com.bookit.model.Room;

@Service
public class PrintFormater {
	
	@Autowired
	private RoomController roomCtrl;
	@Autowired
	private DateFormat df;

	public String buildDisplayRoomLook(Room room) {
		String displayRoom = room.getName();
		displayRoom += "(" + getState(room) + ") - " ;
		displayRoom += (room.isItroom() ? "salle informatique" : "salle de cours") + " - " + room.getL10n() + " : ";
		displayRoom += "Nombre de places : " + room.getCapacity();
		
		return displayRoom;
	}

	public String buildDisplayRoomSee(Room room) {
		String displayRoom = "Salle " + room.getName();
		displayRoom += "\nSalle informatique : " + (room.isItroom() ? "Oui" : "Non");
		displayRoom += "\nLocalisation : " + room.getL10n();
		displayRoom += "\nÉtat actuel : " + getState(room);
		displayRoom += "\nNombre de place : " + room.getCapacity();
		
		return displayRoom;
	}

	public String buildDisplayBook(Book b) {
		String displayBook = df.format(b.getStartDate()) + " - " + df.format(b.getEndDate());
		displayBook += " : " + (b.isPriv() ? "ouverte" : "occupée");
		displayBook += " - " + b.getDescription() + " (" + b.getOwner() + ")";

		return displayBook;
	}

	private String getState(Room r) {
		long start = System.currentTimeMillis();
		long end = start + 1;
		boolean available = roomCtrl.isAvailable(r, start, end, false);
		boolean opened = roomCtrl.isOpened(r, start);
		return available ? opened ? "ouverte" : "libre" : "occupée";
	}
}