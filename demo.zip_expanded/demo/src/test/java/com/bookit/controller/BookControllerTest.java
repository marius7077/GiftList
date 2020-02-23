package com.bookit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
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
	private static final Command cmdA = new Command(true, false, false, false, 0, 0, 0, "", null, "");
	private static final Command cmdD = new Command(false, false, false, false, janv2020_01, mars2020_01, 0, "", null, "");
	private List<Book> init, listD;
	
	@Spy
	@InjectMocks
	private BookController bookCtrl;
	
	@Mock
	private Room room;
	
	@Before
	public void setUp() {
		init = new ArrayList<>();
		init.add(b1);
		init.add(b2);
		init.add(b3);

		listD = new ArrayList<>();
		listD.add(b2);
		
		MockitoAnnotations.initMocks(this);
		
		when(room.getBookList()).thenReturn(init);
	}
	
	@Test
	public void testGetBooksToDisplay() {
		doReturn(listD).when(bookCtrl).getBooksInInterval(any(Room.class), anyLong(), anyLong());
		List<Book> testA = bookCtrl.getBooksToDisplay(room, cmdA);
		Assert.assertEquals(testA, init);
		
		List<Book> testD = bookCtrl.getBooksToDisplay(room, cmdD);
		Assert.assertEquals(testD, listD);
	}
	
	@Test
	public void testGetBooksInInterval() {
		doReturn(false).when(bookCtrl).inInterval(b1, janv2020_01, mars2020_01);
		doReturn(true).when(bookCtrl).inInterval(b2, janv2020_01, mars2020_01);
		doReturn(false).when(bookCtrl).inInterval(b3, janv2020_01, mars2020_01);
		
		List<Book> testD = bookCtrl.getBooksInInterval(room, janv2020_01, mars2020_01);
		Assert.assertEquals(testD, listD);
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
