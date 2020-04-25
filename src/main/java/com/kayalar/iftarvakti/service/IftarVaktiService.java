package com.kayalar.iftarvakti.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.http.HttpException;

import com.kayalar.iftarvakti.model.DayInfo;
import com.kayalar.iftarvakti.model.RemainingTime;
import com.kayalar.iftarvakti.model.RemainingTime.RemainigTimeType;
import com.kayalar.iftarvakti.requester.Requester;

public class IftarVaktiService {

	private Cache cache;
	private Requester requester;

	private static final int CACHE_MAX = 10_000;

	public IftarVaktiService() {
		cache = new Cache();
		requester = new Requester();
	}

	public RemainingTime askForCity(String cityName) {

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

			RemainigTimeType type;
			int remainingHour, remainingMinute, remainingSecond;
			LocalDateTime pivot;

			if (now.isAfter(todayImsak) && now.isBefore(todayIftar)) {
				type = RemainigTimeType.IFTAR;
				pivot = todayIftar;
			} else {
				type = RemainigTimeType.IMSAK;
				pivot = tomorrowImsak;
			}

			int duration = (int) Math.abs(Duration.between(pivot, now).getSeconds());
			remainingSecond = duration % 60;

			int temp = duration / 60;
			remainingMinute = temp % 60;
			remainingHour = temp / 60;

			return new RemainingTime(type, remainingHour, remainingMinute, remainingSecond);
		} catch (NumberFormatException | IOException | URISyntaxException | HttpException e) {
			e.printStackTrace();
			return null;
		}
	}
}
