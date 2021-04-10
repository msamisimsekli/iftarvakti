package com.kayalar.iftarvakti.model;

public class City {

	private String cityName;
	private DailyPrayTimes dayInfo;

	public City(String cityName, DailyPrayTimes dayInfo) {
		this.cityName = cityName;
		this.dayInfo = dayInfo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public DailyPrayTimes getDailyPrayTimes() {
		return dayInfo;
	}

	public void setDailyPrayTimes(DailyPrayTimes dayInfo) {
		this.dayInfo = dayInfo;
	}
}
