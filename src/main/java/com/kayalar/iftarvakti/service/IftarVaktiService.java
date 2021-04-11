package com.kayalar.iftarvakti.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpException;

import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.http.DailyPrayInfoRequester;
import com.kayalar.iftarvakti.http.NotificationSender;
import com.kayalar.iftarvakti.model.DailyPrayTimes;
import com.kayalar.iftarvakti.model.FastEnum;
import com.kayalar.iftarvakti.model.RemainingTime;
import com.kayalar.iftarvakti.model.User;
import com.kayalar.iftarvakti.scheduler.ReminderTask;

public class IftarVaktiService {

	private Cache cache;
	private DailyPrayInfoRequester dailyPrayInfoRequester;
	private NotificationSender notificationSender;

	private static final String errorMessage = "Beklenmeyen sonuç. Daha sonra tekrar deneyiniz.";

	public IftarVaktiService(Configurations config) throws UnknownHostException {
		cache = new Cache(config.getDbAdress(), config.getDbPort(), config.getDbName(), "city", "user");
		dailyPrayInfoRequester = new DailyPrayInfoRequester(config);
		notificationSender = new NotificationSender(config.getBotToken());

		initReminderScheduler();
	}

	public String askForCity(String cityName) {

		try {

			DailyPrayTimes todayInfo = IftarVaktiHelper.getDailyPrayTimes(cache, dailyPrayInfoRequester, cityName);

			RemainingTime rt = IftarVaktiHelper.getRemainingTime(todayInfo);
			int duration = rt.getRemainingDurationInSec();
			FastEnum fastEnum = rt.getFastEnum();
			ZonedDateTime pivot = rt.getTime();

			return IftarVaktiHelper.handsomeResultString(fastEnum, duration, pivot.getHour(), pivot.getMinute(),
					cityName);
		} catch (NumberFormatException | IOException | URISyntaxException | HttpException e) {
			e.printStackTrace();
			return errorMessage;
		}
	}

	public void setCityForUser(Integer userId, String cityName) {
		cache.setCityForUser(userId, cityName);
	}

	public void disableIftarReminderForUser(Integer userId) {
		cache.disableIftarReminderForUser(userId);
	}

	public void disableImsakReminderForUser(Integer userId) {
		cache.disableImsakReminderForUser(userId);
	}

	public void setIftarReminderForUser(Integer userId, int reminderTime) {
		cache.setIftarReminderForUser(userId, reminderTime);
	}

	public void setImsakReminderForUser(Integer userId, int reminderTime) {
		cache.setImsakReminderForUser(userId, reminderTime);
	}

	public User getUser(int userId) {
		return cache.getUser(userId).clone();
	}

	public void addUser(User user) {
		cache.addUser(user);
	}

	private void initReminderScheduler() {

		ReminderTask reminderTask = new ReminderTask(cache, dailyPrayInfoRequester, notificationSender);

		ZonedDateTime now = ZonedDateTime.now();
		int second = now.getSecond();
		int delay = 60 - second;

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(reminderTask, delay, 60, TimeUnit.SECONDS);
	}
}
