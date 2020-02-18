package com.bookit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bookit.model.User;
import com.bookit.service.JSONManager;

@Controller
public class UserController {

	@Autowired
	private JSONManager json;
	
	public User connect(String login, String password) {
		return json.readUsers().stream().filter(u -> login.equals(u.getLogin()) && password.equals(u.getPassword())).findAny().orElse(null);
	}
}