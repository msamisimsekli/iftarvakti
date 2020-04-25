package com.kayalar.iftarvakti.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kayalar.iftarvakti.model.DayInfo;

public class Cache {

	// (K,V) -> (sehir ismi, günlere göre vakit bilgi mapi)
	Map<String, Map<String, DayInfo>> cache;

	public Cache() {
		cache = new HashMap<String, Map<String, DayInfo>>();
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

		cache.put(cityName, dayInfoMap);
	}

	public void clear() {
		cache = new HashMap<String, Map<String, DayInfo>>();
	}

	private String convertToDayId(int day, int month, int year) {
		return day + "" + month + "" + year;
	}
}
