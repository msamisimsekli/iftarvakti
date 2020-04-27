package com.kayalar.iftarvakti.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class UserManagement {

	Map<Integer, UserInfo> userMap;

	public UserManagement() {
		File file = new File("data");
		if (!file.exists()) {
			file.mkdir();
			userMap = new HashMap<Integer, UserInfo>();
			return;
		}

		try {
			Type REVIEW_TYPE = new TypeToken<Map<Integer, UserInfo>>() {
			}.getType();

			JsonReader reader = new JsonReader(new FileReader("data/users.json"));
			userMap = new Gson().fromJson(reader, REVIEW_TYPE);
		} catch (FileNotFoundException e) {
			userMap = new HashMap<Integer, UserInfo>();
		}
	}

	public UserInfo getUserInfo(Integer userId) {
		return userMap.get(userId);
	}

	public void saveUserInfo(UserInfo userInfo) {
		userMap.put(userInfo.getUserId(), userInfo);
	}

	public void saveMap() {
		File file = new File("data");
		if (!file.exists())
			file.mkdir();

		try {
			FileWriter fileWriter = new FileWriter("data/users.json");
			Gson gson = new Gson();
			gson.toJson(userMap, fileWriter);
			fileWriter.close();
		} catch (IOException e) {
		}
	}
}
