package client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class UserManager {

	public User connect(String login, String pwd) {
		return getAllUsers().stream().filter(u -> login.equals(u.getLogin()) && pwd.equals(u.getPassword())).findAny().orElse(null);
	}

	private List<User> getAllUsers() {
		Gson gson = new Gson();
		Type listUser = new TypeToken<List<User>>(){}.getType();
		try {
			return gson.fromJson(new FileReader("../users.json"), listUser);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}