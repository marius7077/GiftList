package com.bookit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.bookit.controller.Command;
import com.bookit.controller.RoomController;
import com.bookit.model.Book;
import com.bookit.model.Room;

public class PrintFormaterTest {
	private static final long janv202001 = 1577833200000L;
	private static final long fevr202001 = 1580511600000L;
	private String displayRoomLook = "R1, somewhere (1 places) - salle informatique : libre";
	private String displayRoomSee = 
		"Salle R1\n"
		+ "Salle informatique : Oui\n"
		+ "Localisation : Somewhere\n"
		+ "État actuel : Libre\n"
		+ "Nombre de places : 1";
	private String displayBook = "2020-01-01_00:00 - 2020-02-01_00:00 : privé - Cours (Jean-Test)";
	private Room room = new Room("R1", true, "Somewhere", 1);
	private Book book = new Book(janv202001, fevr202001, "Jean-Test", "Cours", true);
		
	
	@Spy
	@InjectMocks
	private PrintFormater printer;

	@Mock
	private DateFormat df;
	
	@Mock
	private RoomController roomCtrl;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testBuildDisplayRoomLook() {
		doReturn("libre").when(printer).getState(eq(room), any(Command.class));
		Assert.assertEquals(displayRoomLook, printer.buildDisplayRoomLook(room, new Command()));
	}
	
	@Test
	public void testBuildDisplayRoomSee() {
		doReturn("libre").when(printer).getState(eq(room), any(Command.class));
		Assert.assertEquals(displayRoomSee, printer.buildDisplayRoomSee(room));
	}
	
	@Test
	public void testBuildDisplayBook() {
		Date d1 = new Date(janv202001);
		//Don't work calling when(df.format(d1)).thenReturn(...)
		when(df.format(eq(d1), any(), any())).thenReturn(new StringBuffer("2020-01-01_00:00"));
		Date d2 = new Date(fevr202001);
		//Don't work calling when(df.format(d1)).thenReturn(...)
		when(df.format(eq(d2), any(), any())).thenReturn(new StringBuffer("2020-02-01_00:00"));
		Assert.assertEquals(displayBook, printer.buildDisplayBook(book));
	}
	
	@Test
	public void testGetState() {
		when(roomCtrl.isAccessible(eq(room), anyLong(), anyLong(), eq(false))).thenReturn(true, true, false);
		when(roomCtrl.isBooked(eq(room), anyLong())).thenReturn(true, false, true);
		Assert.assertEquals("ouverte", printer.getState(room, new Command()));
		Assert.assertEquals("libre", printer.getState(room, new Command()));
		Assert.assertEquals("occupée", printer.getState(room, new Command()));
	}
	
	@Test
	public void testToFormatCase(){
		Assert.assertEquals("Test",  printer.toFormatCase("test"));
	}
	
}