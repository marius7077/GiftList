package com.bookit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bookit.model.User;
import com.bookit.service.JSONManager;

@Controller
public class UserController {

	@Autowired
	private JSONManager json;
	
	/**
	 * Try to connect a user using a login and a password
	 * @param login : The user's login
	 * @param password : The user's password
	 * @return User : A User object, or null if there is no user matching
	 */
	public User connect(String login, String password) {
		//Get the first user which have this pair(login, password)
		return json.readUsers().stream()
				.filter(
						u -> login.equals(u.getLogin()) 
						&& password.equals(u.getPassword())
				).findAny().orElse(null);
	}
}