package com.bookit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.bookit.exception.UnfoundRoomException;
import com.bookit.model.Book;
import com.bookit.model.Room;
import com.bookit.service.JSONManager;

public class RoomControllerTest {
	private static final long janv202001 = 1577833200000L;
	private static final long fevr202001 = 1580511600000L;
	private static List<Room> init = new ArrayList<>();
	private static List<Room> testClosed = new ArrayList<>();
	private static List<Room> testIT = new ArrayList<>();
	private static List<Room> testN = new ArrayList<>();
	private static List<Room> testMultiOption = new ArrayList<>();
	private static List<Book> listBook = new ArrayList<>();
	private static Room r1 = new Room("R1", true, "loc1", 100);
	private static Room r2 = new Room("R2", true, "loc1", 10);
	private static Room r3 = new Room("R3", false, "loc1", 60);
	private static Room r4 = new Room("R4", true, "loc1", 5);
	private static Book b1 = new Book();
	
	@Spy
	@InjectMocks
	private RoomController roomCtrl;
	
	@Mock
	private JSONManager json;
	
	@Mock
	private BookController bookCtrl;
	
	@Mock
	private ArrayList<Book> mockListBook;
	
	@Before
	public void setUp() {		
		init.add(r1);
		init.add(r2);
		init.add(r3);
		init.add(r4);

		listBook.add(b1);
		
		testClosed.add(r1);
		testClosed.add(r3);
		testClosed.add(r4);
		testIT.add(r1);
		testIT.add(r4);
		testN.add(r1);
		testN.add(r3);	
		testMultiOption.add(r1);
		
		MockitoAnnotations.initMocks(this);
		
		when(json.readRooms()).thenReturn(init);
		when(bookCtrl.inInterval(any(Book.class), anyLong(), anyLong())).thenReturn(true);
		when(bookCtrl.getBooksInInterval(any(Room.class), anyLong(), anyLong())).thenReturn(listBook);
	}
	
	
	@Test
	public void testList() {
		Command cmdAll = new Command(true, false, false, false, janv202001, fevr202001, 0, "", null, "");
		Command cmdClosed = new Command(false, true, false, false, janv202001, fevr202001, 0, "", null, "");
		Command cmdIT = new Command(false, false, true, false, janv202001, fevr202001, 0, "", null, "");
		Command cmdN = new Command(false, false, false, false, janv202001, fevr202001, 50, "", null, "");
		Command cmdMultiOption = new Command(false, true, true, false, janv202001, fevr202001, 50, "", null, "");
		
		doReturn(true).when(roomCtrl).isAccessible(any(Room.class), anyLong(), anyLong(), anyBoolean());
		doReturn(false).when(roomCtrl).isAccessible(eq(r2), anyLong(), anyLong(), anyBoolean());
		
		Assert.assertEquals(init, roomCtrl.list(cmdAll));
		Assert.assertEquals(testClosed, roomCtrl.list(cmdClosed));
		Assert.assertEquals(testIT, roomCtrl.list(cmdIT));
		Assert.assertEquals(testN, roomCtrl.list(cmdN));
		Assert.assertEquals(testMultiOption, roomCtrl.list(cmdMultiOption));		
	}
	
	@Test
	public void testGetRoomByName() throws UnfoundRoomException {
		Command cmd = new Command();
		cmd.setRoom(r1);
		Assert.assertEquals(r1, roomCtrl.getRoomByName("R1"));
	}
	
	@Test
	public void testCheckBookability() throws UnfoundRoomException {
		when(bookCtrl.getBooksInInterval(eq(r2), anyLong(), anyLong())).thenReturn(new ArrayList<>());
		
		r1.getBookList().removeAll(r1.getBookList());
		r1.getBookList().add(b1);
		
		Command cmd = new Command();
		cmd.setRoom(r1);
		Assert.assertFalse(roomCtrl.checkBookability(cmd));
		cmd.setRoom(r2);
		Assert.assertTrue(roomCtrl.checkBookability(cmd));
	}
	
	@Test
	public void testBook() {
		r1.setBookList(mockListBook);
		roomCtrl.book(r1, b1);
		verify(mockListBook).add(b1);
	}
	
	@Test
	public void testIsAccessible() {
		r1.setBookList(new ArrayList<Book>());
		Assert.assertTrue(roomCtrl.isAccessible(r1, janv202001, janv202001, false));
		r1.getBookList().add(b1);
		Assert.assertTrue(roomCtrl.isAccessible(r1, janv202001, janv202001, false));
		Assert.assertFalse(roomCtrl.isAccessible(r1, janv202001, janv202001, true));
	}
	
	@Test(expected = UnfoundRoomException.class)
	public void testGetRoomByNameWithFalseName() throws UnfoundRoomException {
		roomCtrl.getRoomByName("R5");
		
		//To be sure exception was raised before
		Assert.assertTrue(false);
	}
	
	@Test
	public void testIsBooked() {
		r1.setBookList(new ArrayList<Book>());
		Assert.assertFalse(roomCtrl.isBooked(r1, janv202001));
		r1.getBookList().add(b1);
		Assert.assertTrue(roomCtrl.isBooked(r1, janv202001));
		when(bookCtrl.getBooksInInterval(any(Room.class), anyLong(), anyLong())).thenReturn(new ArrayList<>());
		Assert.assertFalse(roomCtrl.isBooked(r1, janv202001));
	}
}
