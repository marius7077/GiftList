package client;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("mess_bvn");

		RoomManager rm = new RoomManager();
		AppConsole ac = new AppConsole(System.in, rm);
		ac.start();
	}
}
