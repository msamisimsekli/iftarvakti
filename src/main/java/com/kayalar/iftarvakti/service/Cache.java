package com.kayalar.iftarvakti.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.kayalar.iftarvakti.model.DayInfo;

public class Cache {

	// (K,V) -> (sehir ismi, vakit bilgileri)
	private Map<String, DayInfo> cache;
	private String filePath;

	public Cache() {
		File file = new File("data/days");
		if (!file.exists()) {
			file.mkdirs();
			cache = new HashMap<String, DayInfo>();
			return;
		}

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		String dateStr = ft.format(dNow);
		filePath = String.format("data/days/%s", dateStr);

		file = new File(filePath);
		if (!file.exists()) {
			cache = new HashMap<String, DayInfo>();
			return;
		}

		try {
			Type REVIEW_TYPE = new TypeToken<Map<String, DayInfo>>() {
			}.getType();

			JsonReader reader = new JsonReader(new FileReader(filePath));
			cache = new Gson().fromJson(reader, REVIEW_TYPE);
		} catch (FileNotFoundException e) {
			cache = new HashMap<String, DayInfo>();
		}
	}

	public DayInfo getIfExists(String cityName) {
		return cache.get(cityName);
	}

	public void save(String cityName, DayInfo dayInfo) {
		cache.put(cityName, dayInfo);
	}

	public void saveCache() {
		File file = new File("data/days");
		if (!file.exists()) {
			file.mkdirs();
		}

		try {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
			String dateStr = ft.format(dNow);
			String newFilePath = String.format("data/days/%s", dateStr);

			// it means now it is a new day. do not save the cache
			if (!newFilePath.equals(filePath)) {
				cache = new HashMap<String, DayInfo>();
				return;
			}

			FileWriter fileWriter = new FileWriter(filePath);
			Gson gson = new Gson();
			gson.toJson(cache, fileWriter);
			fileWriter.close();
		} catch (IOException e) {
		}
	}
}
