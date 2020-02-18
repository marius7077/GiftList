package com.bookit.model;

public class User {
	private String login;
	private String password;
	private String name;
	private boolean authorized;
	
	public User() {}

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public boolean isAuthorized() { return this.authorized; }
	public void setAuthorized(boolean authorized) { this.authorized = authorized; }
}
