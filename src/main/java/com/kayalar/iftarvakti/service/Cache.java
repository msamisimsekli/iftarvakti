package com.kayalar.iftarvakti.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kayalar.iftarvakti.model.DayInfo;

public class Cache {

	// (K,V) -> (sehir ismi, günlere göre vakit bilgi mapi)
	private Map<String, Map<String, DayInfo>> cache;

	private int numberOfItems;

	public Cache() {
		cache = new HashMap<String, Map<String, DayInfo>>();
		numberOfItems = 0;
	}

	public DayInfo getIfExists(String cityName, int day, int month, int year) {
		if (!cache.containsKey(cityName))
			return null;

		String dayId = convertToDayId(day, month, year);

		if (!cache.get(cityName).containsKey(dayId))
			return null;

		return cache.get(cityName).get(dayId);
	}

	public void save(String cityName, List<DayInfo> dayInfoList) {
		Map<String, DayInfo> dayInfoMap = new HashMap<String, DayInfo>();

		for (DayInfo dayInfo : dayInfoList) {
			String dayId = convertToDayId(dayInfo.getDay(), dayInfo.getMonth(), dayInfo.getYear());
			dayInfoMap.put(dayId, dayInfo);
		}

		int prevSize = cache.get(cityName).size();
		cache.put(cityName, dayInfoMap);
		int afterSize = cache.get(cityName).size();

		numberOfItems += (afterSize - prevSize);
	}

	public int size() {
		return numberOfItems;
	}

	public void clear() {
		cache = new HashMap<String, Map<String, DayInfo>>();
	}

	private String convertToDayId(int day, int month, int year) {
		return day + "" + month + "" + year;
	}
}
