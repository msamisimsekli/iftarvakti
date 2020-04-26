package com.kayalar.iftarvakti;

import java.io.IOException;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.kayalar.iftarvakti.config.ConfigurationReader;
import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.telegram.IftarVaktiBot;

public class IftarVaktiApp {

	public static void main(String[] args) {

		try {
			Configurations config = new ConfigurationReader().getPropValues();

			ApiContextInitializer.init();

			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			telegramBotsApi.registerBot(new IftarVaktiBot(config));

		} catch (TelegramApiException | IOException e) {
			e.printStackTrace();
		}
	}

}
