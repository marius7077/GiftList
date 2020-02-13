package com.bookit;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bookit.controller.ConsoleApplication;

@SpringBootApplication
public class BookITApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookITApplication.class, args);
	}
	
	@Bean
	public InputStream getReader() { return System.in; }
	
	@Bean(name="printer")
	public PrintStream getWriter() { return System.out; }
	
	@Bean(name="error")
	public PrintStream getErrorWriter() { return System.err; }
	
	@Bean(name="date10")
	public DateFormat getDateFormat10() { return new SimpleDateFormat("yyyy-MM-dd_HH:mm"); }

	@Autowired
	private ConsoleApplication console;
	
	@Override
	public void run(String... args) throws Exception { console.start(); }

}
