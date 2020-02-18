package com.bookit.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bookit.model.Room;

public class Command {
	private String method;
	private String room;
	private boolean allOption;
	private boolean closedOption;
	private boolean itOption;
	private boolean privateOption;
	private Long startDate;
	private Long endDate;
	private int nbPers;
	
	public Command() {
		this.allOption = false;
		this.closedOption = false;
		this.itOption = false;
		this.privateOption = false;
		
		this.startDate = System.currentTimeMillis();
		this.endDate = startDate + 1;
		
		this.nbPers = 0;
	}

	public String getMethod() { return this.method; }
	public void setMethod(String method) { this.method = method; }
	public String getRoom() { return this.room; }
	public void setRoom(String room) { this.room = room; }
	public boolean getAllOption() { return this.allOption; }
	public void setAllOption(boolean all) { this.allOption = all; }
	public boolean getClosedOption() { return this.closedOption; }
	public void setClosedOption(boolean closed) { this.closedOption = closed; }
	public boolean getITOption() { return this.itOption; }
	public void setITOption(boolean it) { this.itOption = it; };
	public boolean getPrivateOption() { return this.privateOption; }
	public void setPrivateOption(boolean priv) { this.privateOption = priv; };
	public Long getStartDate() { return this.startDate; }
	public void setStartDate(Long start) { this.startDate = start; }
	public Long getEndDate() { return this.endDate; }	
	public void setEndDate(Long end) { this.endDate = end; }
	public int getNbPers() { return this.nbPers; }
	public void setNbPers(int nb) { this.nbPers = nb; }
}
