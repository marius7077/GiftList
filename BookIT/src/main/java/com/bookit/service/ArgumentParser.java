package com.bookit.service;

import com.bookit.controller.Command;
import com.bookit.controller.RoomController;
import com.bookit.exception.CommandException;
import com.bookit.exception.UnfoundRoomException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ArgumentParser {
	@Value("#{'${listMethod}'.split(',')}")
	private String[] listMethod;
	@Value("#{'${listOptions}'.split(',')}")
	private String[] listOptions;
	@Value("#{'${descOptions}'.split(',')}")
	private String[] descOptions;
	@Value("#{'${bookOptions}'.split(',')}")
	private String[] bookOptions;
	@Value("${argumentParserHelp}")
	private String help;

	@Autowired
	private DateFormat df;
	
	@Autowired
	private RoomController roomCtrl;
	
	/**
	 * Parse a user entry into a command object
	 * @param line : The user entry 
	 * @return Command : The parsed command object
	 * @throws CommandException : Raised if the entry is not valid
	 * @throws UnfoundRoomException : Raised if the room does not exist
	 */
	public Command parseArgs(String line) throws CommandException, UnfoundRoomException {
		Command command = new Command();
		String[] args = line.split(" ");
		if (args.length > 0 && Arrays.asList(listMethod).contains(args[0])) {
			command.setMethod(args[0]);
		} else if (args.length > 0 && args[0].equals(help)) {
			command.setMethod(help);
			if(args.length > 1 && Arrays.asList(listMethod).contains(args[1])) {
				command.setHelp(args[1]);
			}
			return command;
		} else throw new CommandException();
		buildCommand(command, args);
		
		return command;
	}

	/**
	 * Build the command object from the arguments of the entry
	 * @param command : The command object to update
	 * @param args : The line arguments
	 * @throws CommandException : Raised if the entry is not valid
	 * @throws UnfoundRoomException : Raised if the room does not exist
	 */
	protected void buildCommand(Command command, String[] args) throws 
		CommandException, 
		UnfoundRoomException {
		
		int iterator = 1;
		String options = "";
		String method = command.getMethod();
		if(args.length > 1 && '-' == args[iterator].charAt(0)) {
			options = args[iterator++].substring(1);
			if(!correctOptions(command.getMethod(), options.split(""))) {
				throw new CommandException(command.getMethod());
			}
			buildBooleanOptions(command, options);
		}
		try {
			if(this.listMethod[1].equals(method) || this.listMethod[2].equals(method)) {
				command.setRoom(roomCtrl.getRoomByName(args[iterator++]));
			}
			if(options.contains("d") || this.listMethod[2].equals(method)) {
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

	/**
	 * Check if the entry options are valid
	 * @param method : The action chosen by the user
	 * @param options : The option written by the user
	 * @return boolean : True if options are valid, false if not
	 */
	protected boolean correctOptions(String method, String[] options) {
		if(options.length == 0) return false;
		if(method.equals(this.listMethod[0])
			&& Arrays.asList(options).stream()
			.anyMatch(o -> !String.join("", listOptions).contains(o))
		) {
			return false;
		}
		if(method.equals(this.listMethod[1])
			&& Arrays.asList(options).stream()
			.anyMatch(o -> !String.join("", descOptions).contains(o))
		) {
			return false;
		}
		return !method.equals(this.listMethod[2])
				|| Arrays.asList(options).stream()
				.noneMatch(o -> !String.join("", bookOptions).contains(o));
	}

	/**
	 * Set the command object's boolean properties from the selected options
	 * @param command : The command object to update
	 * @param options : The option written by the user
	 */
	protected void buildBooleanOptions(Command command, String options) {
		if(options.contains("a")) command.setAllOption(true);
		if(options.contains("c")) command.setClosedOption(true);
		if(options.contains("i")) command.setITOption(true);
		if(options.contains("p")) command.setPrivateOption(true);	
	}

	/**
	 * Try to parse the date interval of the user entry
	 * @param command : The command object to update
	 * @param d : The date interval
	 * @throws ParseException : Raised if the date interval is not parsable
	 */
	protected void buildDateOptions(Command command, String d) throws ParseException {
		String[] dates = d.split(";");
		command.setStartDate(df.parse(dates[0]).getTime());
		command.setEndDate(df.parse(dates[1]).getTime());
	}
}