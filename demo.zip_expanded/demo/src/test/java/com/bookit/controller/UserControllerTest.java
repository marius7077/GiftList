package com.bookit.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bookit.model.User;
import com.bookit.service.JSONManager;

public class UserControllerTest {
	private static List<User> listUser = new ArrayList<>();
	private static User u1 = new User("l1", "p1", "n1", true);
	private static User u2 = new User("l2", "p2", "n2", false);
	
	@InjectMocks
	private UserController userCtrl;
	
	@Mock
	private JSONManager json;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		listUser.add(u1);
		listUser.add(u2);
		
		when(json.readUsers()).thenReturn(listUser);
		
	}

	@Test
	public void testConnect() {
		User test = userCtrl.connect("l1", "p1");
		Assert.assertEquals(u1, test);
		User testfail = userCtrl.connect("", "");
		Assert.assertNull(testfail);
		User testUnauth = userCtrl.connect("l2", "p2");
		Assert.assertNull(testfail);
	}
}
