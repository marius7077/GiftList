package client;

import java.text.ParseException;

public class Main {
	public static void main(String[] args)  {
		System.out.println("mess_bvn");
		AppConsole ac = new AppConsole(System.in);
		try {
			ac.start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
