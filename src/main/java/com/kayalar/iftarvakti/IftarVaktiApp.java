package com.kayalar.iftarvakti;

import java.io.IOException;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.kayalar.iftarvakti.telegram.IftarVaktiBot;

public class IftarVaktiApp {

	public static void main(String[] args) {
		try {

			ApiContextInitializer.init();
			IftarVaktiBot bot = new IftarVaktiBot();
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			telegramBotsApi.registerBot(bot);

		} catch (TelegramApiException | IOException e) {
			e.printStackTrace();
		}
	}

}
