package client;

public class Book {
	private long startDate;
	private long endDate;
	private String description;
	private String owner;
	private boolean accessible;

	public Book() {}
	
	public long getStartDate() {return startDate;}
	public void setStartDate(long startDate) {this.startDate = startDate;}
	public long getEndDate() {return endDate;}
	public void setEndDate(long endDate) {this.endDate = endDate;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public String getOwner() {return owner;}
	public void setOwner(String owner) {this.owner = owner;}
	public boolean isAccessible() {return accessible;}
	public void setAccessible(boolean accessible) {this.accessible = accessible;}
}
