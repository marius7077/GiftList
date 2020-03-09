package com.bookit.config;

import com.bookit.controller.Command;
import com.bookit.controller.ConsoleApplication;
import com.bookit.exception.CommandException;
import com.bookit.exception.ExceptionManager;
import com.bookit.exception.UnfoundRoomException;
import com.bookit.model.Room;
import com.bookit.model.User;
import com.bookit.service.ArgumentParser;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookITConfiguration implements CommandLineRunner {

	@Bean(name="reader")
	public InputStream getReader() { return System.in; }
	
	@Bean(name="printer")
	public Logger getWriter() { return Logger.getLogger(BookITConfiguration.class.getName()); }
	
	@Bean(name="date")
	public DateFormat getDateFormat16() { return new SimpleDateFormat("yyyy-MM-dd_HH:mm"); }
	
	@Bean(name="listroom")
	public Type getListRoomType() { return new TypeToken<List<Room>>(){}.getType(); }
	
	@Bean(name="listuser")
	public Type getListUserType() { return new TypeToken<List<User>>(){}.getType(); }
	
	@Autowired
	private ConsoleApplication console;
	@Autowired
	private ArgumentParser parser;
	@Autowired
	private ExceptionManager exceptionCtrl;
	
	@Override
  	public void run(String... args)  {
		if(args.length > 0) {
			Command c;
			try {
				c = parser.parseArgs(String.join(" ", args));
		  		console.execute(c, new Scanner(getReader()));
			} catch (CommandException | UnfoundRoomException e) {
				exceptionCtrl.exceptionCtrl(e);
			}
	  	} else {
	  		console.start(); 
		} 
  	}
}
