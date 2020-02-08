package client;

import java.util.List;

public class Room {
	private String name;
	private boolean itroom;
	private String l10n;
	private int capacity;
	private List<Book> bookList;
	
	public Room() {}

	public String getName() {return name;}
	public void setName(String name) { this.name = name; }
	public boolean isItroom() { return itroom; }
	public void setItroom(boolean itroom) { this.itroom = itroom; }
	public String getL10n() { return l10n; }
	public void setL10n(String l10n) { this.l10n = l10n; }
	public int getCapacity() { return capacity; }
	public void setCapacity(int capacity) { this.capacity = capacity; }
	public List<Book> getBookList() {return bookList;}
	public void setBookList(List<Book> bookList) {this.bookList = bookList;}
}