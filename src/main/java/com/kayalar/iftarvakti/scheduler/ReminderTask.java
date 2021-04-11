package com.kayalar.iftarvakti.scheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;

import com.kayalar.iftarvakti.http.DailyPrayInfoRequester;
import com.kayalar.iftarvakti.http.NotificationSender;
import com.kayalar.iftarvakti.model.City;
import com.kayalar.iftarvakti.model.DailyPrayTimes;
import com.kayalar.iftarvakti.model.FastEnum;
import com.kayalar.iftarvakti.model.RemainingTime;
import com.kayalar.iftarvakti.model.User;
import com.kayalar.iftarvakti.service.Cache;
import com.kayalar.iftarvakti.service.IftarVaktiHelper;

public class ReminderTask implements Runnable {

	private Cache cache;
	private DailyPrayInfoRequester dailyPrayInfoRequester;
	private NotificationSender notificationSender;

	public ReminderTask(Cache cache, DailyPrayInfoRequester dailyPrayInfoRequester,
			NotificationSender notificationSender) {
		this.cache = cache;
		this.dailyPrayInfoRequester = dailyPrayInfoRequester;
		this.notificationSender = notificationSender;
	}

	@Override
	public void run() {

		try {

			Map<String, RemainingTime> cityRemainingTimeMap = new HashMap<String, RemainingTime>();

			for (City city : cache.getCities()) {
				String cityName = city.getCityName();
				DailyPrayTimes todayInfo = IftarVaktiHelper.getDailyPrayTimes(cache, dailyPrayInfoRequester, cityName);
				RemainingTime rt = IftarVaktiHelper.getRemainingTime(todayInfo);
				cityRemainingTimeMap.put(cityName, rt);
			}

			for (User user : cache.getUsers()) {

				String cityName = user.getCity();
				if (cityName == null)
					continue;

				RemainingTime rt = cityRemainingTimeMap.get(cityName);
				FastEnum fastEnum = rt.getFastEnum();
				ZonedDateTime pivot = rt.getTime();
				int remainingTimeInSec = rt.getRemainingDurationInSec();
				int remainingTimeInMin = remainingTimeInSec / 60;

				int timer;
				String message;

				switch (fastEnum) {
				case IFTAR:
					timer = user.getIftarReminderTimer();
					message = IftarVaktiHelper.handsomeResultString(fastEnum, remainingTimeInSec, pivot.getHour(),
							pivot.getMinute(), cityName);
					if (user.isIftarReminderEnabled() && timer == remainingTimeInMin) {
						notificationSender.sendNotification(user.getChatId(), message);
					}
					break;
				case IMSAK:
					timer = user.getImsakReminderTimer();
					message = IftarVaktiHelper.handsomeResultString(fastEnum, remainingTimeInSec, pivot.getHour(),
							pivot.getMinute(), cityName);
					if (user.isImsakReminderEnabled() && timer == remainingTimeInMin) {
						notificationSender.sendNotification(user.getChatId(), message);
					}
					break;
				}
			}

		} catch (NumberFormatException | IOException | URISyntaxException | HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
