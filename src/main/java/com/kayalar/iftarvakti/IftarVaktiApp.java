package com.kayalar.iftarvakti;

import com.kayalar.iftarvakti.service.IftarVaktiService;
import com.kayalar.iftarvakti.telegram.IftarVaktiBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class IftarVaktiApp {

	public static void main(String[] args) {
		IftarVaktiService service = new IftarVaktiService();

		ApiContextInitializer.init();

		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

		try {
			telegramBotsApi.registerBot(new IftarVaktiBot());

		} catch (
				TelegramApiException e) {
			e.printStackTrace();
		}

		String rt = service.askForCity("İSTANBUL");

		System.out.println(rt);
	}

}
