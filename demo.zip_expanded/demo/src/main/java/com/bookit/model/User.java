package com.bookit.model;

/**
 * Representation of an user
 */
public class User {
	private String login;
	private String password;
	private String name;
	private boolean authorized;
	
	public User() {}
	
	public User(String login, String password, String name, boolean authorized) {
		this.login = login;
		this.password = password;
		this.name = name;
		this.authorized = authorized;
	}

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public boolean isAuthorized() { return this.authorized; }
	public void setAuthorized(boolean authorized) { this.authorized = authorized; }
}
