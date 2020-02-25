package com.bookit.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookit.controller.Command;
import com.bookit.controller.RoomController;
import com.bookit.exception.CommandException;
import com.bookit.exception.UnfoundRoomException;

@Service
public class ArgumentParser {
	private final static String[] METHOD = { "list", "describe", "book" };
	private final static String[] LIST_OPTIONS = { "a", "c", "i", "d", "n" };
	private final static String[] DESC_OPTIONS = { "a", "d" };
	private final static String[] BOOK_OPTIONS = { "p" };
	private final static String HELP = "help";

	@Autowired
	private DateFormat df;
	
	@Autowired
	private RoomController roomCtrl;
	
	public Command parseArgs(String line) throws CommandException, UnfoundRoomException {
		Command command = new Command();
		String[] args = line.split(" ");

		if (args.length > 0 && Arrays.asList(METHOD).contains(args[0])) {
			command.setMethod(args[0]);
		} else if (args.length > 0 && args[0].equals(HELP)) {
			command.setMethod(HELP);
			if(args.length > 1 && Arrays.asList(METHOD).contains(args[1])) {
				command.setHelp(args[1]);
			}
			return command;
		} else throw new CommandException();
		buildCommand(command, args);
		
		return command;
	}

	protected void buildCommand(Command command, String[] args) throws 
		CommandException, 
		UnfoundRoomException {
		
		int iterator = 1;
		String options = "", method = command.getMethod();
		if(args.length > 1 && '-' == args[iterator].charAt(0)) {
			options = args[iterator++].substring(1);
			if(!correctOptions(command.getMethod(), options.split(""))) {
				throw new CommandException(command.getMethod());
			}
			buildBooleanOptions(command, options);
		}
		try {
			if(METHOD[1].equals(method) || METHOD[2].equals(method)) {
				command.setRoom(roomCtrl.getRoomByName(args[iterator++]));
			}
			if(options.contains("d") || METHOD[2].equals(method)) {
				buildDateOptions(command, args[iterator++]);	
			}
			if(options.contains("n")) {
				command.setNbPers(Integer.parseInt(args[iterator++]));
			}
			if(iterator != args.length) throw new CommandException(method);
		} catch(IndexOutOfBoundsException | ParseException | NumberFormatException e) { 
			throw new CommandException(method); 
		}
	}

	protected boolean correctOptions(String method, String[] options) {
		if(options.length == 0) return false;
		if(method.equals(METHOD[0]) 
			&& Arrays.asList(options).stream()
			.anyMatch(o -> !String.join("",  LIST_OPTIONS).contains(o))
		) {
			return false;
		}
		if(method.equals(METHOD[1]) 
			&& Arrays.asList(options).stream()
			.anyMatch(o -> !String.join("",  DESC_OPTIONS).contains(o))
		) {
			return false;
		}
		if(method.equals(METHOD[2]) 
			&& Arrays.asList(options).stream()
			.anyMatch(o -> !String.join("",  BOOK_OPTIONS).contains(o))
		) {
			return false;
		}
		return true;
	}

	protected void buildBooleanOptions(Command command, String options) {
		if(options.contains("a")) command.setAllOption(true);
		if(options.contains("c")) command.setClosedOption(true);
		if(options.contains("i")) command.setITOption(true);
		if(options.contains("p")) command.setPrivateOption(true);	
	}

	protected void buildDateOptions(Command command, String d) throws ParseException {
		String[] dates = d.split(";");
		command.setStartDate(df.parse(dates[0]).getTime());
		command.setEndDate(df.parse(dates[1]).getTime());
	}
}