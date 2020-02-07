package client;

public class User {
	private String login;
	private String password;
	private String name;
	private String status;
	private boolean locked;
	
	public User() {}

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public boolean isLocked() { return false; }
	public void setLocked(boolean locked) { this.locked = locked; }
}
