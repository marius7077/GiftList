package com.bookit.model;

/**
 * Representation of a room booking
 */
public class Book {
	private long startDate;
	private long endDate;
	private String owner;
	private String description;
	private boolean priv;

	public Book() {}

	public Book(long startDate, long endDate, String login, String description, boolean privateOption) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.owner = login;
		this.description = description;
		this.priv = privateOption;
	}

	public String getOwner() { return owner; }
	public void setOwner(String owner) { this.owner = owner; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public boolean isPriv() { return priv; }
	public void setPriv(boolean priv) { this.priv = priv; }
	public long getStartDate() { return this.startDate; }
	public void setStartDate(long startDate) { this.startDate = startDate; }
	public long getEndDate() { return this.endDate; }
	public void setEndDate(long endDate) { this.endDate = endDate; }
}
