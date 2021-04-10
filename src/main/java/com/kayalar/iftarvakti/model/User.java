package com.kayalar.iftarvakti.model;

public class User {

	private Integer userId;
	private String city;
	private Long chatId;

	public User(Integer userId, String city, Long chatId) {
		super();
		this.userId = userId;
		this.city = city;
		this.chatId = chatId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}
}
