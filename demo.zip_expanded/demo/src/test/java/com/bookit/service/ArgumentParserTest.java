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
	private static final String methodList = "list";
	private static final String methodDesc = "describe";
	private static final String methodBook = "book";
	private static final String methodHelp = "help";
	private static final String dateInterval = "2020-01-01_00:00;2020-02-01_00:00";
	private static final long janv202001 = 1577833200000L;
	private static final long fevr202001 = 1580511600000L;
	private static Room r1 = new Room("R1", false, "", 0);
	private static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
	
	
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
		
		Command cmdList = parser.parseArgs(methodList);
		Command cmdDesc = parser.parseArgs(methodDesc);
		Command cmdBook = parser.parseArgs(methodBook);
		Command cmdHelp = parser.parseArgs(methodHelp);
		Command cmdHelpMethod = parser.parseArgs(methodHelp + " " + methodList);

		verify(parser, times(3)).buildCommand(any(Command.class), any());
		Assert.assertEquals(methodList, cmdList.getMethod());
		Assert.assertEquals(methodDesc, cmdDesc.getMethod());
		Assert.assertEquals(methodBook, cmdBook.getMethod());
		Assert.assertEquals(methodHelp, cmdHelp.getMethod());
		Assert.assertEquals(methodHelp, cmdHelpMethod.getMethod());
		Assert.assertEquals(methodList, cmdHelpMethod.getHelp());
	}
	
	@Test(expected = CommandException.class)
	public void testParseArgsWithInvalidMethod() throws CommandException, UnfoundRoomException {
		Command cmdBlank = parser.parseArgs(" ");
		
		//To be sure exception was raised before
		Assert.assertTrue(false);
	}
	
	@Test
	public void testBuildCommand() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		
		String[] argsList = { methodList };
		Command cmdList = mock(Command.class);
		doCallRealMethod().when(cmdList).setMethod(anyString());
		cmdList.setMethod(methodList);
		parser.buildCommand(cmdList, argsList);
		verify(cmdList, times(0)).setRoom(any(Room.class));
		verify(cmdList, times(0)).setNbPers(anyInt());
		
		String[] argsListOpt = { methodList, "-opt"};
		Command cmdListOpt = new Command();
		cmdListOpt.setMethod(methodList);
		parser.buildCommand(cmdListOpt, argsListOpt);
		verify(parser, times(1)).buildBooleanOptions(cmdListOpt, "opt");
		Assert.assertNull(cmdListOpt.getRoom());
		
		String[] argsDescDN = { methodDesc, "-dn", "R1", "<date_interval>", "1" };
		Command cmdDescDN = new Command();
		cmdDescDN.setMethod(methodDesc);
		parser.buildCommand(cmdDescDN, argsDescDN);
		verify(parser, times(1)).buildDateOptions(cmdDescDN, "<date_interval>" );
		Assert.assertEquals(r1,  cmdDescDN.getRoom());
		Assert.assertEquals(1,  cmdDescDN.getNbPers());
		
		String[] argsDescN = { methodDesc, "-n", "R1", "1" };
		Command cmdDescN = new Command();
		cmdDescN.setMethod(methodDesc);
		parser.buildCommand(cmdDescN, argsDescN);
		Assert.assertEquals(r1,  cmdDescDN.getRoom());
		Assert.assertEquals(1,  cmdDescDN.getNbPers());
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandBookWithoutRoom() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { methodBook };
		Command c = new Command();
		c.setMethod(methodBook);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandBookWithoutDate() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { methodBook, "R1" };
		Command c = new Command();
		c.setMethod(methodBook);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithoutDate() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { methodList, "-d" };
		Command c = new Command();
		c.setMethod(methodList);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithoutNB() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { methodList, "-n" };
		Command c = new Command();
		c.setMethod(methodList);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithUnparsableNB() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { methodList, "-n", "bonjour" };
		Command c = new Command();
		c.setMethod(methodList);
		parser.buildCommand(c,  args);
	}
	
	@Test(expected = CommandException.class)
	public void testBuildCommandListWithToManyArgs() throws CommandException, UnfoundRoomException, ParseException {
		doReturn(true).when(parser).correctOptions(anyString(), any());
		doNothing().when(parser).buildDateOptions(any(Command.class), anyString());
		doNothing().when(parser).buildBooleanOptions(any(Command.class), anyString());
		String[] args = { methodList, "bonjour" };
		Command c = new Command();
		c.setMethod(methodList);
		parser.buildCommand(c,  args);
	}
	
	@Test
	public void testCorrectoptions() {
		String methodBlank = "";
		String[] optionsBlank = {};		
		String[] optionsList = { "a", "c", "i", "d", "n" };
		String[] optionsDesc = { "a", "d" };
		String[] optionsBook = { "p" };
		String[] falseOptionsList = { "a", "c", "i", "d", "n", "p" };
		String[] falseOptionsDesc = { "a", "d", "i" };
		String[] falseOptionsBook = { "p", "a" };
	
		Assert.assertFalse(parser.correctOptions(methodBlank, optionsBlank));
		Assert.assertFalse(parser.correctOptions(methodList, falseOptionsList));
		Assert.assertFalse(parser.correctOptions(methodDesc, falseOptionsDesc));
		Assert.assertFalse(parser.correctOptions(methodBook, falseOptionsBook));
		Assert.assertTrue(parser.correctOptions(methodList, optionsList));
		Assert.assertTrue(parser.correctOptions(methodDesc, optionsDesc));
		Assert.assertTrue(parser.correctOptions(methodBook, optionsBook));
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
		Date d1 = dateParser.parse("2020-01-01_00:00");
		Date d2 = dateParser.parse("2020-02-01_00:00");
		when(df.parse("2020-01-01_00:00")).thenReturn(d1);
		when(df.parse("2020-02-01_00:00")).thenReturn(d2);
		
		parser.buildDateOptions(c, dateInterval);
		Assert.assertEquals(janv202001, c.getStartDate());
		Assert.assertEquals(fevr202001, c.getEndDate());
		
		when(df.parse("bonjour")).thenThrow(ParseException.class);
		parser.buildDateOptions(c, "bonjour");
	}

}
