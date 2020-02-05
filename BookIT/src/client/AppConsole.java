package client;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AppConsole {
	Scanner sc;
	RoomManager rm;
	ArgumentParser ap;

	public AppConsole(InputStream input, RoomManager rm) {
		this.sc = new Scanner(input);
		this.rm = new RoomManager();
		this.ap = new ArgumentParser();
	}

	public void start() throws ParseException {
		String line;
		while(true) {
			System.out.print("> ");	
			line = sc.nextLine();
			if(!"".equals(line)) {
				Map<String, String> commandMap = ap.parseArgs(line);
				this.execute(commandMap);
			}
		}
	}
	
	private void execute(Map<String, String> commandMap) throws ParseException {
		switch(commandMap.get("method")) {
			case "l": 
				List<Room> rooms = rm.look(commandMap); 
				for(Room r : rooms) {
					String displayRoom = r.getName();
					displayRoom += "(" + (rm.isAvailable(r, System.currentTimeMillis(), 0, false) ? (rm.isOpened(r, System.currentTimeMillis()) ? "ouverte" : "libre") : "occupée") + ") - " ;
					displayRoom += (r.isItroom() ? "salle informatique" : "salle de cours") + " - " + r.getL10n() + " : ";
					displayRoom += "Nombre de places : " + r.getCapacity();
					System.out.println(displayRoom);
				}
				break;
			case "s": System.out.println(rm.see(commandMap)); break;
			case "b": System.out.println(rm.book(commandMap)); break;
			default: System.out.println(this.help());
		}
	}
		
	public String help() {return "str_help";}
}