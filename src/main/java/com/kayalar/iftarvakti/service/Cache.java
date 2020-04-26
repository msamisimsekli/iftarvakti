package com.kayalar.iftarvakti.service;

import java.util.HashMap;
import java.util.Map;

import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.model.DayInfo;

public class Cache {

	// (K,V) -> (sehir ismi, vakit bilgileri)
	private Map<String, DayInfo> cache;

	public Cache(Configurations config) {
		cache = new HashMap<String, DayInfo>();
	}

	public DayInfo getIfExists(String cityName) {
		return cache.get(cityName);
	}

	public void save(String cityName, DayInfo dayInfo) {
		cache.put(cityName, dayInfo);
	}

	public void clear() {
		cache = new HashMap<String, DayInfo>();
	}
}
