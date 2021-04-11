package com.kayalar.iftarvakti.service;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;
import com.kayalar.iftarvakti.model.Cities;
import com.kayalar.iftarvakti.model.City;
import com.kayalar.iftarvakti.model.DailyPrayTimes;
import com.kayalar.iftarvakti.model.User;
import com.kayalar.iftarvakti.model.Users;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;

public class Cache {

	private DBCollection cityTable;
	private DBCollection userTable;

	private Cities cities;
	private Users users;

	private String currentDayStr;

	private Gson gson;

	public Cache(String dbAdress, int dbPort, String dbName, String cityTableName, String userTableName)
			throws UnknownHostException {

		MongoClient mongoClient = new MongoClient(
				new MongoClientURI(String.format("mongodb://%s:%s", dbAdress, dbPort)));

		DB database = mongoClient.getDB(dbName);

		cityTable = database.getCollection(cityTableName);
		userTable = database.getCollection(userTableName);

		initCaches();
	}

	public DailyPrayTimes getDailyPrayTimesIfExists(String cityName) {
		String dayId = dateStr();

		if (!currentDayStr.equals(dayId)) {
			currentDayStr = dayId;
			cities = new Cities(currentDayStr);
		}

		City city = cities.getCity(cityName);
		if (city == null)
			return null;

		return city.getDailyPrayTimes();
	}

	public void saveDailyPrayTimesForCity(String cityName, DailyPrayTimes dayInfo) {
		City city = new City(cityName, dayInfo);
		cities.addCity(city);

		BasicDBObject query = new BasicDBObject();
		query.put("dayStr", currentDayStr);

		BasicDBObject obj = (BasicDBObject) JSON.parse(gson.toJson(cities));
		cityTable.update(query, obj, true, false);// if exists update, if not insert
	}

	public Collection<User> getUsers() {
		return users.getUsers();
	}

	public Collection<City> getCities() {
		return cities.getCities();
	}

	public User getUser(Integer userId) {
		return users.getUser(userId);
	}

	public void addUser(User user) {
		users.addUser(user);
		BasicDBObject obj = (BasicDBObject) JSON.parse(gson.toJson(user));
		userTable.insert(obj);
	}

	public void setCityForUser(Integer userId, String cityName) {
		User user = users.getUser(userId);
		user.setCity(cityName);
		updateUserInDB(user);
	}

	public void disableIftarReminderForUser(Integer userId) {
		User user = users.getUser(userId);
		user.disableIftarReminder();
		updateUserInDB(user);
	}

	public void disableImsakReminderForUser(Integer userId) {
		User user = users.getUser(userId);
		user.disableImsakReminder();
		updateUserInDB(user);
	}

	public void setIftarReminderForUser(Integer userId, int reminderTime) {
		User user = users.getUser(userId);
		user.setIftarReminderTimer(reminderTime);
		updateUserInDB(user);
	}

	public void setImsakReminderForUser(Integer userId, int reminderTime) {
		User user = users.getUser(userId);
		user.setImsakReminderTimer(reminderTime);
		updateUserInDB(user);
	}

	private String dateStr() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
		return format.format(now);
	}

	private void initCaches() {

		gson = new Gson();
		currentDayStr = dateStr();

		initCities();
		initUsers();
	}

	private void initCities() {
		BasicDBObject query = new BasicDBObject();
		query.put("dayStr", currentDayStr);

		DBCursor dbCursor = cityTable.find(query);

		// we expect only one document
		DBObject obj = dbCursor.next();

		cities = gson.fromJson(JSON.serialize(obj), Cities.class);
	}

	private void initUsers() {
		users = new Users();

		DBCursor dbCursor = userTable.find();

		while (dbCursor.hasNext()) {
			DBObject obj = dbCursor.next();
			User user = gson.fromJson(JSON.serialize(obj), User.class);
			users.addUser(user);
		}
	}

	private void updateUserInDB(User user) {
		BasicDBObject query = new BasicDBObject();
		query.put("userId", user.getUserId());

		BasicDBObject obj = (BasicDBObject) JSON.parse(gson.toJson(user));
		userTable.update(query, obj, false, false);
	}
}
