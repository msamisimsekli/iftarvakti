package com.kayalar.iftarvakti.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.http.HttpException;

import com.kayalar.iftarvakti.model.DayInfo;
import com.kayalar.iftarvakti.requester.Requester;

public class IftarVaktiService {

	private Cache cache;
	private Requester requester;

	private static final int CACHE_MAX = 10_000;

	public IftarVaktiService() {
		cache = new Cache();
		requester = new Requester();
	}

	public String askForCity(String cityName) {

		try {
			// Time of now
			LocalDateTime now = LocalDateTime.now();

			int day = now.getDayOfMonth();
			int month = now.getMonthValue();
			int year = now.getYear();

			// Tomorrow
			LocalDateTime tomorrow = now.plusDays(1);

			int tomorrowDay = tomorrow.getDayOfMonth();
			int tomorrowMonth = tomorrow.getMonthValue();
			int tomorrowYear = tomorrow.getYear();

			DayInfo todayInfo = cache.getIfExists(cityName, day, month, year);
			DayInfo tomorrowInfo = cache.getIfExists(cityName, tomorrowDay, tomorrowMonth, tomorrowYear);

			if (todayInfo == null) {

				if (cache.size() >= CACHE_MAX) {
					cache.clear();
				}

				List<DayInfo> dayInfoList = requester.requestForList(cityName);

				// at least today and tomorrow is required
				if (dayInfoList.size() < 2)
					return null;

				cache.save(cityName, dayInfoList);

				todayInfo = dayInfoList.get(0);
				tomorrowInfo = dayInfoList.get(1);
			}

			LocalDateTime todayImsak = LocalDateTime.of(todayInfo.getYear(), todayInfo.getMonth(), todayInfo.getDay(),
					todayInfo.getImsakHour(), todayInfo.getImsakMinute());
			LocalDateTime todayIftar = LocalDateTime.of(todayInfo.getYear(), todayInfo.getMonth(), todayInfo.getDay(),
					todayInfo.getAksamHour(), todayInfo.getAksamMinute());

			LocalDateTime tomorrowImsak = LocalDateTime.of(tomorrowInfo.getYear(), tomorrowInfo.getMonth(),
					tomorrowInfo.getDay(), tomorrowInfo.getImsakHour(), tomorrowInfo.getImsakMinute());

			String remainingTimetype;
			int remainingHour, remainingMinute, remainingSecond;
			LocalDateTime pivot;

			if (now.isAfter(todayImsak) && now.isBefore(todayIftar)) {
				remainingTimetype = "İftar";
				pivot = todayIftar;
			} else {
				remainingTimetype = "İmsak";
				pivot = tomorrowImsak;
			}

			int duration = (int) Math.abs(Duration.between(pivot, now).getSeconds());
			remainingSecond = duration % 60;

			int temp = duration / 60;
			remainingMinute = temp % 60;
			remainingHour = temp / 60;

			return handSomeResultString(remainingTimetype, remainingHour, remainingMinute, remainingSecond,
					pivot.getHour(), pivot.getMinute());
		} catch (NumberFormatException | IOException | URISyntaxException | HttpException e) {
			e.printStackTrace();
			return "Beklenmeyen sonuç. Daha sonra tekrar deneyiniz.";
		}
	}

	private String handSomeResultString(String type, int hour, int minute, int second, int pivotHour, int pivotMinute) {
		String remainingStr = "";

		if (hour != 0)
			remainingStr += (hour + " saat, ");

		if (minute != 0)
			remainingStr += (minute + " dakika, ");

		if (second != 0)
			remainingStr += (second + " saniye");

		String pivotStr = "";

		if (pivotHour < 10)
			pivotStr += "0";
		pivotStr += pivotHour + ":";

		if (pivotMinute < 10)
			pivotStr += "0";
		pivotStr += pivotMinute;

		return String.format("%s için kalan süre: %s\n%s vakti: %s", type, remainingStr, type, pivotStr);
	}
}
