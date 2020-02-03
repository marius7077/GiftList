package client;

import java.io.InputStream;
import java.util.Scanner;

public class AppConsole {
	Scanner sc;
	RoomManager rm;

	public AppConsole(InputStream input, RoomManager rm) {
		this.sc = new Scanner(input);
		this.rm = rm;
	}

	public void start() {
		String line;
		while(true) {
			System.out.print(">");	
			line = sc.nextLine();
			if(!"".equals(line)) parseLine(line);
		}
	}
	
	public String help() {
		return "str_help";
	}
	
	private void parseLine(String line) {
		String[] command = line.split(" ");
		switch(command[0]) {
			case "look":
				rm.look(command);
				break;
			case "book":
				rm.book(command);
				break;
			case "see":
				rm.see(command);
				break;
			default:
				System.out.println(help());
				break;
		}
	}
}