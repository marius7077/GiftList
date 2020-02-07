package client;

import java.io.FileNotFoundException;
import java.text.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Main {
	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		System.out.println("mess_bvn");
		RoomManager rm = new RoomManager();
		AppConsole ac = new AppConsole(System.in, rm);
		try {
			ac.start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
