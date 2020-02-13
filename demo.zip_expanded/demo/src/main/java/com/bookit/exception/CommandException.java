package com.bookit.exception;

public class CommandException extends Exception {
	private String method;
	
	public CommandException(String method) {
		this.method = method;
	}

	public CommandException() {}

	public String getMethod() { return method; }
}