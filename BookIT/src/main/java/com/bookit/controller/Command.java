package com.bookit.controller;

import com.bookit.model.Room;

/**
 * Object containing all information about a user command
 */
public class Command {
	private String method;
	private Room room;
	private boolean allOption;
	private boolean closedOption;
	private boolean itOption;
	private boolean privateOption;
	private long startDate;
	private long endDate;
	private int nbPers;
	private String help;
	
	public Command() {
		this.allOption = false;
		this.closedOption = false;
		this.itOption = false;
		this.privateOption = false;
		this.startDate = System.currentTimeMillis();
		this.endDate = startDate + 1;
		this.nbPers = 0;
		this.method = "";
		this.room = null;
		this.help = "";
	}
	
	public Command(boolean a, boolean c, boolean i, boolean p, long startDate, long endDate, int n, String method, Room room, String help) {
		this.allOption = a;
		this.closedOption = c;
		this.itOption = i;
		this.privateOption = p;
		this.startDate = startDate;
		this.endDate = endDate;
		this.nbPers = n;
		this.method = method;
		this.room = room;
		this.help = help;
	}

	public String getMethod() { return this.method; }
	public void setMethod(String method) { this.method = method; }
	public Room getRoom() { return this.room; }
	public void setRoom(Room room) { this.room = room; }
	public boolean getAllOption() { return this.allOption; }
	public void setAllOption(boolean all) { this.allOption = all; }
	public boolean getClosedOption() { return this.closedOption; }
	public void setClosedOption(boolean closed) { this.closedOption = closed; }
	public boolean getITOption() { return this.itOption; }
	public void setITOption(boolean it) { this.itOption = it; }

  public boolean getPrivateOption() { return this.privateOption; }
	public void setPrivateOption(boolean priv) { this.privateOption = priv; }

  public long getStartDate() { return this.startDate; }
	public void setStartDate(Long start) { this.startDate = start; }
	public long getEndDate() { return this.endDate; }	
	public void setEndDate(Long end) { this.endDate = end; }
	public int getNbPers() { return this.nbPers; }
	public void setNbPers(int nb) { this.nbPers = nb; }
	public String getHelp() { return this.help; }
	public void setHelp(String help) { this.help = help; }
}
