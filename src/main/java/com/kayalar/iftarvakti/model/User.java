package com.kayalar.iftarvakti.model;

public class User implements Cloneable {

	private int userId;
	private String city;
	private String firstName;
	private String userName;
	private String lastName;
	private Long chatId;
	private int iftarReminderTimer;
	private int imsakReminderTimer;

	public User(int userId, Long chatId, String firstName, String lastName, String userName) {
		this.userId = userId;
		this.chatId = chatId;
		this.firstName = firstName;
		this.userName = userName;
		this.lastName = lastName;
		this.iftarReminderTimer = 0;
		this.imsakReminderTimer = 0;
	}

	public User(int userId, String city, String firstName, String userName, String lastName, Long chatId,
			int iftarReminderTimer, int imsakReminderTimer) {
		super();
		this.userId = userId;
		this.city = city;
		this.firstName = firstName;
		this.userName = userName;
		this.lastName = lastName;
		this.chatId = chatId;
		this.iftarReminderTimer = iftarReminderTimer;
		this.imsakReminderTimer = imsakReminderTimer;
	}

	public int getUserId() {
		return userId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getChatId() {
		return chatId;
	}

	public int getIftarReminderTimer() {
		return iftarReminderTimer;
	}

	public void setIftarReminderTimer(int iftarReminderTimer) {
		this.iftarReminderTimer = iftarReminderTimer;
	}

	public int getImsakReminderTimer() {
		return imsakReminderTimer;
	}

	public void setImsakReminderTimer(int imsakReminderTimer) {
		this.imsakReminderTimer = imsakReminderTimer;
	}

	public boolean isIftarReminderEnabled() {
		return iftarReminderTimer != 0;
	}

	public void disableIftarReminder() {
		iftarReminderTimer = 0;
	}

	public boolean isImsakReminderEnabled() {
		return imsakReminderTimer != 0;
	}

	public void disableImsakReminder() {
		imsakReminderTimer = 0;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getUserName() {
		return userName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public User clone() {
		User clone = new User(userId, city, firstName, userName, lastName, chatId, iftarReminderTimer,
				imsakReminderTimer);
		return clone;
	}
}
