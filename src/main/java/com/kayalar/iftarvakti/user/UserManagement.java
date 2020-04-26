package com.kayalar.iftarvakti.user;

import java.util.HashMap;
import java.util.Map;

public class UserManagement {

	Map<Integer, UserInfo> userMap;

	public UserManagement() {
		userMap = new HashMap<Integer, UserInfo>();
	}

	public UserInfo getUserInfo(Integer userId) {
		return userMap.get(userId);
	}

	public void saveUserInfo(UserInfo userInfo) {
		userMap.put(userInfo.getUserId(), userInfo);
	}
}
