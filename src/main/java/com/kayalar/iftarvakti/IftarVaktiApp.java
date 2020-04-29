package com.kayalar.iftarvakti;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.kayalar.iftarvakti.config.ConfigurationReader;
import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.telegram.IftarVaktiBot;

@SpringBootApplication
public class IftarVaktiApp {

	public static void main(String[] args) {
		try {

			ApiContextInitializer.init();
			SpringApplication.run(IftarVaktiApp.class, args);
			Configurations config = new ConfigurationReader().getPropValues();
			IftarVaktiBot bot = new IftarVaktiBot(config);
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			telegramBotsApi.registerBot(bot);

			Timer t = new Timer();
			// This task is scheduled to run every 10 seconds

			t.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					bot.saveData();
				}
			}, 0, 1000 * 60 * 10); // each 10 minute, save data

		} catch (TelegramApiException | IOException e) {
			e.printStackTrace();
		}
	}

}
