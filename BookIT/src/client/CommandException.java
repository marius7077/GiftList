package client;

public class CommandException extends Exception {
	public CommandException() { 
		System.err.println("Commande incorrecte. Entrez \"help\" pour plus d'informations.");
	}
	
	public CommandException(String method) { 
		System.err.println("Commande incorrecte. Entrez \"help " + method + "\" pour plus d'informations.");
	}
}