package com.kayalar.iftarvakti.telegram;

import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.service.IftarVaktiService;
import com.kayalar.iftarvakti.user.UserInfo;
import com.kayalar.iftarvakti.user.UserManagement;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

public class IftarVaktiBot extends TelegramLongPollingBot {

	private List<String> cities = Arrays.asList(new String[] { "ADANA", "ADIYAMAN", "AFYONKARAHİSAR", "AGRI", "AMASYA",
			"ANKARA", "ANTALYA", "ARTVIN", "AYDIN", "BALIKESIR", "BILECIK", "BINGOL", "BITLIS", "BOLU", "BURDUR",
			"BURSA", "CANAKKALE", "CANKIRI", "CORUM", "DENIZLI", "DIYARBAKIR", "EDIRNE", "ELAZIG", "ERZINCAN",
			"ERZURUM", "ESKISEHIR", "GAZIANTEP", "GIRESUN", "GUMUSHANE", "HAKKARI", "HATAY", "ISPARTA", "ICEL",
			"ISTANBUL", "IZMIR", "KARS", "KASTAMONU", "KAYSERI", "KIRKLARELI", "KIRSEHIR", "KOCAELI", "KONYA",
			"KUTAHYA", "MALATYA", "MANISA", "KAHRAMANMARAS", "MARDIN", "MUGLA", "MUS", "NEVSEHIR", "NIGDE", "ORDU",
			"RIZE", "SAKARYA", "SAMSUN", "SIIRT", "SINOP", "SIVAS", "TEKIRDAG", "TOKAT", "TRABZON", "TUNCELI",
			"SANLIURFA", "USAK", "VAN", "YOZGAT", "ZONGULDAK", "AKSARAY", "BAYBURT", "KARAMAN", "KIRIKKALE", "BATMAN",
			"SIRNAK", "BARTIN", "ARDAHAN", "IGDIR", "YALOVA", "KARABUK", "KILIS", "OSMANIYE", "DUZCE" });

	IftarVaktiService service;
	Configurations config;
	UserManagement userManagement;

	public IftarVaktiBot(Configurations config) {
		this.config = config;
		service = new IftarVaktiService(config);
		userManagement = new UserManagement();
	}

	public void onUpdateReceived(Update update) {

		SendMessage message = new SendMessage();
		message.setText(responseForUpdate(update));
		message.setChatId(update.getMessage().getChatId());

		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private String responseForUpdate(Update update) {
		String command = update.getMessage().getText();

		System.out.println(String.format("User=%s sent command=%s", update.getMessage().getFrom().getId(), command));
		System.out.println(update);

		// TODO: Commandlist db conf olup cache'de tutulacak
		if (command.equals("/start")) {

			return "Hoşgeldiniz! /help yazarak bot hakkında bilgi alabilirsiniz.";
		}

		if (command.equals("/help")) {

			String str1 = "1) İftar bilgisini öğrenmek istediğiniz şehrin adını ya da plaka numarasını yazıp gönderebilirsiniz. Ör:Ankara ya da 34 vb.";
			String str2 = "2) Bulunduğunuz şehrin ismini ya da plaka numarasını /sehir_kaydet komutunu kullanarak kaydedebilir, sonrasında /ogren komutuyla kolayca iftar/imsak bilgisini alabilirsiniz. Ör: /sehir_kaydet istanbul\n/sehir_kaydet komutuna uzun basarak yanına sehir ismini kolaylıkla yazabilirsiniz";

			String text = String.format("%s\n\n%s", str1, str2);

			return text;
		}

		if (command.startsWith("/sehir_kaydet")) {
			String[] split = command.split(" ");
			if (split.length != 2) {
				return "Şehir ismi algılanamadı. Lütfen /sehir_kaydet komudu sonrasında boşluk bırakarak şehir ismini yazınız";
			}

			String cityName;
			try {
				cityName = checkCityId(split[1]);
			} catch (NumberFormatException e) {
				cityName = checkCityName(split[1]);
			}

			if (cityName == null) {
				return command + " isimli şehir bulunamadı.";
			}

			Integer userId = update.getMessage().getFrom().getId();
			Long chatId = update.getMessage().getChatId();

			UserInfo userInfo = new UserInfo(userId, cityName, chatId);
			userManagement.saveUserInfo(userInfo);

			return clearTurkishChars(cityName.toUpperCase()) + " bulunduğunuz şehir olarak sisteme kaydedildi.";
		}

		if (command.equals("/ogren")) {
			Integer userId = update.getMessage().getFrom().getId();
			UserInfo userInfo = userManagement.getUserInfo(userId);

			if (userInfo == null)
				return "Henüz şehirinizi kaydetmemişsiniz. /sehir_kaydet komutu ile şehirinizi kaydedebilirsiniz. Bilgi için /help";

			String cityName = userInfo.getCity();
			return clearTurkishChars(cityName.toUpperCase()) + "\n" + service.askForCity(cityName);
		}

		// Bir şehir ismi girildiğinde burası çalışır

		String cityName;
		try {

			cityName = checkCityId(command);
		} catch (NumberFormatException e) {
			cityName = checkCityName(command);
		}

		if (cityName != null) {
			System.out.println(cityName);
			return clearTurkishChars(cityName.toUpperCase()) + "\n" + service.askForCity(cityName);
		} else {
			return command + " isimli şehir bulunamadı.";
		}
	}

	public String checkCityName(String cityName) {
		cityName = cityName.toLowerCase();
		String clearedCityName = clearTurkishChars(cityName);
		double maxScore = 0;
		int maxCityIndex = -1;

		for (int i = 0; i < cities.size(); i++) {
			String city = cities.get(i);
			city = clearTurkishChars(city.toLowerCase());

			SimilarityStrategy strategy = new JaroWinklerStrategy();
			StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
			double score = service.score(city, clearedCityName);

			if (score > maxScore) {
				maxScore = score;
				maxCityIndex = i;
			}
		}

		if (maxScore > 0.90)
			return cities.get(maxCityIndex);

		return null;
	}

	public String checkCityId(String cityName) {
		int cityId = Integer.parseInt(cityName) - 1;
		if (0 < cityId && cityId < 82)
			return clearTurkishChars(cities.get(cityId).toLowerCase());
		return null;
	}

	// https://gist.github.com/onuryilmaz/6034569
	public static String clearTurkishChars(String str) {
		String ret = str;
		char[] turkishChars = new char[] { 0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F,
				0x11E };
		char[] englishChars = new char[] { 'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G' };
		for (int i = 0; i < turkishChars.length; i++) {
			ret = ret.replaceAll(new String(new char[] { turkishChars[i] }),
					new String(new char[] { englishChars[i] }));
		}
		return ret;
	}

	@Override
	public String getBotUsername() {
		return config.getBotUserId();
	}

	@Override
	public String getBotToken() {
		return config.getBotToken();
	}

	public void saveData() {
		userManagement.saveMap();
		service.saveCache();
	}
}
