package com.kayalar.iftarvakti.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cities {

	// for each city, there will be city dayInfo
	private Map<String, City> cityMap;

	private String dayStr;

	public Cities(String dayStr) {
		this.cityMap = new HashMap<String, City>();
		this.dayStr = dayStr;
	}

	public City getCity(String cityName) {
		return cityMap.get(cityName);
	}

	public void addCity(City city) {
		cityMap.put(city.getCityName(), city);
	}

	public Collection<City> getCities() {
		return cityMap.values();
	}

	public String getDayStr() {
		return dayStr;
	}
}
