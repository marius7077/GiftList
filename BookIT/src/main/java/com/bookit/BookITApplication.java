package com.bookit;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bookit.controller.ConsoleApplication;
import com.bookit.model.Room;
import com.bookit.model.User;
import com.google.gson.reflect.TypeToken;

@SpringBootApplication
public class BookITApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookITApplication.class, args);
	}
	
	@Bean(name="reader")
	public InputStream getReader() { return System.in; }
	
	@Bean(name="printer")
	public PrintStream getWriter() { return System.out; }
	
	@Bean(name="error")
	public PrintStream getErrorWriter() { return System.err; }
	
	@Bean(name="date")
	public DateFormat getDateFormat16() { return new SimpleDateFormat("yyyy-MM-dd_HH:mm"); }
	
	@Bean(name="listroom")
	public Type getListRoomType() { return new TypeToken<List<Room>>(){}.getType(); }
	
	@Bean(name="listuser")
	public Type getListUserType() { return new TypeToken<List<User>>(){}.getType(); }

	@Autowired
	private ConsoleApplication console;
	
	@Override
	public void run(String... args) throws Exception { console.start(); }

}
