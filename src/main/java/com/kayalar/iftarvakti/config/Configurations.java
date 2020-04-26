package com.kayalar.iftarvakti.config;

public class Configurations {

	private String botToken;
	private String botUserId;
	private String dbName;
	private String dbAdress;
	private int dbPort;
	private String cityCacheName;
	private String apiAddress;
	private String apiToken;

	public String getBotToken() {
		return botToken;
	}

	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}

	public String getBotUserId() {
		return botUserId;
	}

	public void setBotUserId(String botUserId) {
		this.botUserId = botUserId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbAdress() {
		return dbAdress;
	}

	public void setDbAdress(String dbAdress) {
		this.dbAdress = dbAdress;
	}

	public int getDbPort() {
		return dbPort;
	}

	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

	public String getCityCacheName() {
		return this.cityCacheName;
	}

	public void setCityCacheName(String cityCacheName) {
		this.cityCacheName = cityCacheName;
	}

	public String getApiAddress() {
		return apiAddress;
	}

	public void setApiAddress(String apiAddress) {
		this.apiAddress = apiAddress;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
}
