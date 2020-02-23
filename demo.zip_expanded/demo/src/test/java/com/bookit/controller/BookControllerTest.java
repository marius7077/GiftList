package com.bookit.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bookit.model.Book;
import com.bookit.model.Room;

public class BookControllerTest {
	private static final long janv2020_01 = 1577833200000L;
	private static final long fevr2020_01 = 1580511600000L;
	private static final long mars2020_01 = 1583017200000L;
	private static final long avri2020_01 = 1585692000000L;
	private static final Book b1 = new Book(janv2020_01, fevr2020_01, "login1", "desc1", false);
	private static final Book b2 = new Book(fevr2020_01, mars2020_01, "login1", "desc1", false);
	private static final Book b3 = new Book(mars2020_01, avri2020_01, "login1", "desc1", false);
	private static final Book b4 = new Book(fevr2020_01, avri2020_01, "login1", "desc1", false);
	private static final Book b5 = new Book(janv2020_01, avri2020_01, "login1", "desc1", false);
	private static final Book b6 = new Book(janv2020_01, mars2020_01, "login1", "desc1", false);
	private List<Book> init;
	
	@Mock
	private BookController bookCtrl;
	
	@Mock
	private Command cmdA;
	
	@Mock
	private Command cmdD;
	
	@Mock
	private Room room;
	
	@Before
	public void setUp() {
		init = new ArrayList<>();
		init.add(b1);
		init.add(b2);
		init.add(b3);
		
		MockitoAnnotations.initMocks(this);
		
		when(cmdA.getAllOption()).thenReturn(true);
		when(cmdD.getAllOption()).thenReturn(false);
		when(cmdD.getStartDate()).thenReturn(janv2020_01);
		when(cmdD.getEndDate()).thenReturn(mars2020_01);
		
		when(room.getBookList()).thenReturn(init);

		when(bookCtrl.getBooksToDisplay(any(Room.class), any(Command.class))).thenCallRealMethod();
		when(bookCtrl.inInterval(any(Book.class), anyLong(), anyLong())).thenCallRealMethod();
	}
	
	@Test
	public void testGetBooksToDisplay() {
		when(bookCtrl.inInterval(b1, janv2020_01, mars2020_01)).thenReturn(true);
		when(bookCtrl.inInterval(b2, janv2020_01, mars2020_01)).thenReturn(true);
		when(bookCtrl.inInterval(b3, janv2020_01, mars2020_01)).thenReturn(false);
		
		List<Book> testA = bookCtrl.getBooksToDisplay(room, cmdA);
		Assert.assertEquals(testA, init);
		
		List<Book> testD = bookCtrl.getBooksToDisplay(room, cmdD);
		Assert.assertNotEquals(testD, init);
		Assert.assertTrue(testD.contains(b1));
		Assert.assertTrue(testD.contains(b2));
		Assert.assertFalse(testD.contains(b3));	
	}
	
	@Test
	public void testInInterval() {
		Assert.assertFalse(bookCtrl.inInterval(b2, janv2020_01, fevr2020_01));
		Assert.assertTrue(bookCtrl.inInterval(b4, janv2020_01, mars2020_01));
		Assert.assertTrue(bookCtrl.inInterval(b2, janv2020_01, avri2020_01));
		Assert.assertTrue(bookCtrl.inInterval(b5, fevr2020_01, mars2020_01));
		Assert.assertTrue(bookCtrl.inInterval(b6, fevr2020_01, avri2020_01));
		Assert.assertFalse(bookCtrl.inInterval(b1, fevr2020_01, mars2020_01));
	}
}
