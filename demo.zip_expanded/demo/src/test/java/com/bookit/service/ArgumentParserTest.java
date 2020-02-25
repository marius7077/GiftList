package com.bookit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.bookit.exception.CommandException;
import com.bookit.exception.UnfoundRoomException;
import com.bookit.model.Room;

public class ArgumentParserTest {
	private static final String method_list = "list";
	private static final String method_desc = "describe";
	private static final String method_book = "book";
	private static final String method_help = "help";
	private static final String date_interval = "2020-01-01_00:00;2020-02-01_00:00";
	private static final long janv2020_01 = 1577833200000L;
	private static final long fevr2020_01 = 1580511600000L;
	private static Room r1 = new Room("R1", false, "", 0);
	private static SimpleDateFormat date_parser = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
	
	
	@Spy
	@InjectMocks
	private ArgumentParser parser;
	
	@Mock
	private DateFormat df;
	
	@Mock
	private RoomController roomCtrl;
	
	@Before
	public void setUp() throws UnfoundRoomException, ParseException {
		MockitoAnnotations.initMocks(this);
		
		when(roomCtrl.getRoomByName(anyString())).thenReturn(r1);
	}

	@Test
	public void testParseArgs() throws CommandException, UnfoundRoomException {	
		doNothing().when(parser).buildCommand(any(Command.class), any());
		
		Command cmd_list = parser.parseArgs(method_list);
		Command cmd_desc = parser.parseArgs(method_desc);
		Command cmd_book = parser.parseArgs(method_book);
		Command cmd_help = parser.parseArgs(method_help);
		Command cmd_help_method = parser.parseArgs(method_help + " " + method_list);

		verify(parser, times(3)).buildCommand(any(Command.class), any());
		Assert.assertEquals(method_list, cmd_list.getMethod());
		Assert.assertEquals(method_desc, cmd_desc.getMethod());
		Assert.assertEquals(method_book, cmd_book.getMethod());
		Assert.assertEquals(method_help, cmd_help.getMethod());
		Assert.assertEquals(method_help, cmd_help_method.getMethod());
		Assert.assertEquals(method_list, cmd_help_method.getHelp());
	}
	
	@Test(expected = CommandException.class)
	public void testParseArgsWithInvalidMethod() throws CommandException, UnfoundRoomException {
		Command cmd_blank = parser.parseArgs(" ");
		
		//To be sure exception was raised before
		Assert.assertTrue(false);
	}
	
	@Test
	public void testBuildCommand() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		
		String[] args_list = { method_list };
		Command cmd_list = mock(Command.class);
		doCallRealMethod().when(cmd_list).setMethod(anyString());
		cmd_list.setMethod(method_list);
		parser.buildCommand(cmd_list, args_list);
		verify(cmd_list, times(0)).setRoom(any(Room.class));
		verify(cmd_list, times(0)).setNbPers(anyInt());
		
		String[] args_list_opt = { method_list, "-opt"};
		Command cmd_list_opt = new Command();
		cmd_list_opt.setMethod(method_list);
		parser.buildCommand(cmd_list_opt, args_list_opt);
		verify(parser, times(1)).buildBooleanOptions(cmd_list_opt, "opt");
		Assert.assertNull(cmd_list_opt.getRoom());
		
		String[] args_desc_DN = { method_desc, "-dn", "R1", "<date_interval>", "1" };
		Command cmd_desc_DN = new Command();
		cmd_desc_DN.setMethod(method_desc);
		parser.buildCommand(cmd_desc_DN, args_desc_DN);
		verify(parser, times(1)).buildDateOptions(cmd_desc_DN, "<date_interval>" );
		Assert.assertEquals(r1,  cmd_desc_DN.getRoom());
		Assert.assertEquals(1,  cmd_desc_DN.getNbPers());
		
		String[] args_desc_N = { method_desc, "-n", "R1", "1" };
		Command cmd_desc_N = new Command();
		cmd_desc_N.setMethod(method_desc);
		parser.buildCommand(cmd_desc_N, args_desc_N);
		Assert.assertEquals(r1,  cmd_desc_DN.getRoom());
		Assert.assertEquals(1,  cmd_desc_DN.getNbPers());
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandBookWithoutRoom() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { method_book };
		Command c = new Command();
		c.setMethod(method_book);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandBookWithoutDate() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { method_book, "R1" };
		Command c = new Command();
		c.setMethod(method_book);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithoutDate() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { method_list, "-d" };
		Command c = new Command();
		c.setMethod(method_list);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithoutNB() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { method_list, "-n" };
		Command c = new Command();
		c.setMethod(method_list);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithUnparsableNB() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { method_list, "-n", "bonjour" };
		Command c = new Command();
		c.setMethod(method_list);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithToManyArgs() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { method_list, "bonjour" };
		Command c = new Command();
		c.setMethod(method_list);
		parser.buildCommand(c,  args);
	}
	
	@Test
	public void testCorrectoptions() {
		String method_blank = "";
		String[] options_blank = {};		
		String[] options_list = { "a", "c", "i", "d", "n" };
		String[] options_desc = { "a", "d" };
		String[] options_book = { "p" };
		String[] false_options_list = { "a", "c", "i", "d", "n", "p" };
		String[] false_options_desc = { "a", "d", "i" };
		String[] false_options_book = { "p", "a" };
	
		Assert.assertFalse(parser.correctOptions(method_blank, options_blank));
		Assert.assertFalse(parser.correctOptions(method_list, false_options_list));
		Assert.assertFalse(parser.correctOptions(method_desc, false_options_desc));
		Assert.assertFalse(parser.correctOptions(method_book, false_options_book));
		Assert.assertTrue(parser.correctOptions(method_list, options_list));
		Assert.assertTrue(parser.correctOptions(method_desc, options_desc));
		Assert.assertTrue(parser.correctOptions(method_book, options_book));
	}
	
	@Test
	public void testBuildBooleanOptions() {
		Command c = new Command();
		Assert.assertFalse(c.getAllOption());
		Assert.assertFalse(c.getClosedOption());
		Assert.assertFalse(c.getITOption());
		Assert.assertFalse(c.getPrivateOption());
		
		parser.buildBooleanOptions(c, "a");
		Assert.assertTrue(c.getAllOption());
		Assert.assertFalse(c.getClosedOption());
		Assert.assertFalse(c.getITOption());
		Assert.assertFalse(c.getPrivateOption());
		
		parser.buildBooleanOptions(c, "c");
		Assert.assertTrue(c.getAllOption());
		Assert.assertTrue(c.getClosedOption());
		Assert.assertFalse(c.getITOption());
		Assert.assertFalse(c.getPrivateOption());
		
		parser.buildBooleanOptions(c, "i");
		Assert.assertTrue(c.getAllOption());
		Assert.assertTrue(c.getClosedOption());
		Assert.assertTrue(c.getITOption());
		Assert.assertFalse(c.getPrivateOption());
		
		parser.buildBooleanOptions(c, "p");
		Assert.assertTrue(c.getAllOption());
		Assert.assertTrue(c.getClosedOption());
		Assert.assertTrue(c.getITOption());
		Assert.assertTrue(c.getPrivateOption());
	}
	
	@Test(expected = ParseException.class)
	public void testBuildDateOptions() throws ParseException {
		Command c = new Command();
		Date d1 = date_parser.parse("2020-01-01_00:00");
		Date d2 = date_parser.parse("2020-02-01_00:00");
		when(df.parse("2020-01-01_00:00")).thenReturn(d1);
		when(df.parse("2020-02-01_00:00")).thenReturn(d2);
		
		parser.buildDateOptions(c, date_interval);
		Assert.assertEquals(janv2020_01, c.getStartDate());
		Assert.assertEquals(fevr2020_01, c.getEndDate());
		
		when(df.parse("bonjour")).thenThrow(ParseException.class);
		parser.buildDateOptions(c, "bonjour");
	}

}
