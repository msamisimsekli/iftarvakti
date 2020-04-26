package com.kayalar.iftarvakti.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.http.HttpException;

import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.model.DayInfo;
import com.kayalar.iftarvakti.requester.Requester;

public class IftarVaktiService {

	private Cache cache;
	private Requester requester;
	private static final String errorMessage = "Beklenmeyen sonuç. Daha sonra tekrar deneyiniz.";

	public IftarVaktiService(Configurations config) {
		cache = new Cache(config);
		requester = new Requester(config);
	}

	public String askForCity(String cityName) {

		try {
			// Time of now
			ZonedDateTime now = ZonedDateTime.now();

			// Tomorrow
			ZonedDateTime tomorrow = now.plusDays(1);

			int tomorrowDay = tomorrow.getDayOfMonth();
			int tomorrowMonth = tomorrow.getMonthValue();
			int tomorrowYear = tomorrow.getYear();

			DayInfo todayInfo = cache.getIfExists(cityName);

			if (todayInfo == null) {

				DayInfo dayInfo = requester.requestForList(cityName);

				// at least today and tomorrow is required
				if (dayInfo == null)
					return errorMessage;

				cache.save(cityName, dayInfo);

				todayInfo = dayInfo;
			}

			String zoneId = "Europe/Istanbul";

			ZonedDateTime todayImsak = ZonedDateTime.of(todayInfo.getYear(), todayInfo.getMonth(), todayInfo.getDay(),
					todayInfo.getImsakHour(), todayInfo.getImsakMinute(), 0, 0, ZoneId.of(zoneId));
			ZonedDateTime todayIftar = ZonedDateTime.of(todayInfo.getYear(), todayInfo.getMonth(), todayInfo.getDay(),
					todayInfo.getAksamHour(), todayInfo.getAksamMinute(), 0, 0, ZoneId.of(zoneId));

			// bir kac dakikadan bisey olmaz. bugunku imsaka bakalım :)
			// yarinki bu api de verilmiyor
			ZonedDateTime tomorrowImsak = ZonedDateTime.of(tomorrowYear, tomorrowMonth, tomorrowDay,
					todayInfo.getImsakHour(), todayInfo.getImsakMinute(), 0, 0, ZoneId.of(zoneId));

			String remainingTimetype;
			int remainingHour, remainingMinute, remainingSecond;
			ZonedDateTime pivot;

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
			return errorMessage;
		}
	}

	public Cache getCache() {
		return cache;
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

		return String.format("%s için kalan süre:\n%s\n%s vakti: %s", type, remainingStr, type, pivotStr);
	}
}
