package com.kayalar.iftarvakti.model;

import java.util.HashMap;
import java.util.Map;

public class Users {

	private Map<Integer, User> userMap;

	public Users() {
		this.userMap = new HashMap<Integer, User>();
	}

	public User getUser(Integer userId) {
		return userMap.get(userId);
	}

	public void addUser(User user) {
		userMap.put(user.getUserId(), user);
	}
}
