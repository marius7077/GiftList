package com.bookit.exception;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionManager extends Exception {

	@Autowired
	private Logger logger;

	public void exceptionCtrl(Exception e) {
		if(e instanceof CommandException){
			CommandException ce = (CommandException) e;
			if( ce.getMethod() != null) logger.log(
					Level.SEVERE, "Commande incorrecte. Entrez \"help " + ce.getMethod() + "\" pour plus d'informations.");
			else logger.log(Level.SEVERE, "Commande incorrecte. Entrez \"help\" pour plus d'informations.\n");
		}
		else if(e instanceof UnfoundRoomException){
			UnfoundRoomException ure = (UnfoundRoomException) e;
			logger.log(Level.SEVERE, "La salle \"" + ure.getRoom() + "\" spécifiée n'existe pas. Affichez la liste des salles existantes avec \"list -a\"");
		}
	}
}
