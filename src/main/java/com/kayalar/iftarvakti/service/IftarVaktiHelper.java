package com.kayalar.iftarvakti.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import com.kayalar.iftarvakti.http.DailyPrayInfoRequester;
import com.kayalar.iftarvakti.model.DailyPrayTimes;
import com.kayalar.iftarvakti.model.FastEnum;
import com.kayalar.iftarvakti.model.RemainingTime;

public class IftarVaktiHelper {

	public static DailyPrayTimes getDailyPrayTimes(Cache cache, DailyPrayInfoRequester dailyPrayInfoRequester,
			String cityName)
			throws NumberFormatException, ClientProtocolException, IOException, URISyntaxException, HttpException {

		DailyPrayTimes todayInfo = cache.getDailyPrayTimesIfExists(cityName);

		if (todayInfo == null) {

			DailyPrayTimes dayInfo = dailyPrayInfoRequester.requestForCity(cityName);

			cache.saveDailyPrayTimesForCity(cityName, dayInfo);

			todayInfo = dayInfo;
		}

		return todayInfo;
	}

	public static RemainingTime getRemainingTime(DailyPrayTimes todayInfo) {
		// Time of now
		ZonedDateTime now = ZonedDateTime.now();

		// Tomorrow
		ZonedDateTime tomorrow = now.plusDays(1);

		int tomorrowDay = tomorrow.getDayOfMonth();
		int tomorrowMonth = tomorrow.getMonthValue();
		int tomorrowYear = tomorrow.getYear();

		String zoneId = "Europe/Istanbul";

		ZonedDateTime todayImsak = ZonedDateTime.of(todayInfo.getYear(), todayInfo.getMonth(), todayInfo.getDay(),
				todayInfo.getImsakHour(), todayInfo.getImsakMinute(), 0, 0, ZoneId.of(zoneId));
		ZonedDateTime todayIftar = ZonedDateTime.of(todayInfo.getYear(), todayInfo.getMonth(), todayInfo.getDay(),
				todayInfo.getAksamHour(), todayInfo.getAksamMinute(), 0, 0, ZoneId.of(zoneId));

		// bir kac dakikadan bisey olmaz. bugunku imsaka bakalım :)
		// yarinki bu api de verilmiyor //TODO
		ZonedDateTime tomorrowImsak = ZonedDateTime.of(tomorrowYear, tomorrowMonth, tomorrowDay,
				todayInfo.getImsakHour(), todayInfo.getImsakMinute(), 0, 0, ZoneId.of(zoneId));

		FastEnum fastEnum;
		ZonedDateTime pivot;

		if (now.isAfter(todayImsak) && now.isBefore(todayIftar)) {
			fastEnum = FastEnum.IFTAR;
			pivot = todayIftar;
		} else {
			fastEnum = FastEnum.IMSAK;
			if (now.isBefore(todayImsak))
				pivot = todayImsak;
			else {
				pivot = tomorrowImsak;
			}
		}

		int duration = (int) Math.abs(Duration.between(pivot, now).getSeconds());

		RemainingTime rt = new RemainingTime(fastEnum, pivot, duration);
		return rt;
	}

	public static String handsomeResultString(FastEnum fastEnum, int duration, int pivotHour, int pivotMinute,
			String cityName) {

		int remainingSecond = duration % 60;
		int temp = duration / 60;
		int remainingMinute = temp % 60;
		int remainingHour = temp / 60;

		String remainingStr = "";

		if (remainingHour != 0)
			remainingStr += (remainingHour + " saat ");

		if (remainingMinute != 0)
			remainingStr += (remainingMinute + " dakika ");

		if (remainingSecond != 0)
			remainingStr += (remainingSecond + " saniye");

		String pivotStr = "";

		if (pivotHour < 10)
			pivotStr += "0";
		pivotStr += pivotHour + ":";

		if (pivotMinute < 10)
			pivotStr += "0";
		pivotStr += pivotMinute;

		return String.format("%s\n%s için kalan süre:\n%s\n%s vakti: %s", cityName, fastEnum, remainingStr, fastEnum,
				pivotStr);
	}
}
