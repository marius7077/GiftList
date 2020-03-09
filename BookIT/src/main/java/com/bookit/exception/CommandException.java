package com.bookit.exception;

public class CommandException extends Exception {
	private final String method;
	
	public CommandException(String method) {
		this.method = method;
	}

	public CommandException() {
		this.method = null;
	}

	public String getMethod() { return method; }
}