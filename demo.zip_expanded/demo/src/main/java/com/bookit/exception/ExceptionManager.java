package com.bookit.exception;

import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class ExceptionManager extends Exception {
	
	@Autowired
	@Qualifier("error")
	private PrintStream error;
	
	public void exceptionCtrl(CommandException e) {
		if(e.getMethod() != null) error.println("Commande incorrecte. Entrez \"help " + e.getMethod() + "\" pour plus d'informations.");
		else error.println("Commande incorrecte. Entrez \"help\" pour plus d'informations.");
	}
	
	public void exceptionCtrl(UnfoundRoomException e) {
		error.println("La salle \"" + e.getRoom() + "\" spécifiée n'existe pas. Affichez la liste des salles existantes avec \"list -a\"");
	}
}
