package com.kayalar.iftarvakti.telegram;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.kayalar.iftarvakti.config.ConfigurationReader;
import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.model.User;
import com.kayalar.iftarvakti.service.IftarVaktiService;

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

	private IftarVaktiService service;

	private Configurations config;

	public IftarVaktiBot() throws IOException {
		this.config = new ConfigurationReader().getPropValues();
		service = new IftarVaktiService(config);
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

		Message message = update.getMessage();
		org.telegram.telegrambots.meta.api.objects.User telegramUser = update.getMessage().getFrom();

		Integer userId = telegramUser.getId();
		Long chatId = message.getChatId();

		User user = service.getUser(userId);
		if (user == null) {
			user = new User(userId, chatId, telegramUser.getFirstName(), telegramUser.getLastName(),
					telegramUser.getUserName());
			service.addUser(user);
		}

		if (command.equals("/start")) {

			return "Hoşgeldiniz! /help yazarak bot hakkında bilgi alabilirsiniz.";
		}

		if (command.equals("/help")) {

			String str1 = "1) İftar bilgisini öğrenmek istediğiniz şehrin adını ya da plaka numarasını yazıp gönderebilirsiniz. Ör:Ankara ya da 34 vb.";
			String str2 = "2) Bulunduğunuz şehrin ismini ya da plaka numarasını /sehir_kaydet komutunu kullanarak kaydedebilir, sonrasında /ogren komutuyla kolayca iftar/imsak bilgisini alabilirsiniz. Ör:\n \t\t\t/sehir_kaydet istanbul\n/sehir_kaydet komutuna uzun basarak yanına sehir ismini kolaylıkla yazabilirsiniz";
			String str3 = "3) /iftar_hatirlatici komutuyla iftar vakitlerinden belirli vakit önce size otomatik hatırlatma mesajı gelmesini sağlayabilirsiniz. Ör:\n \t\t\t/iftar_hatirlatici 30\n Bu komutla iftar zamanlarından 30 dakika önce otomatik mesaj alırsınız.";
			String str4 = "4) /imsak_hatirlatici komutuyla imsak vakitlerinden belirli vakit önce size otomatik hatırlatma mesajı gelmesini sağlayabilirsiniz. Ör:\n \t\t\t/imsak_hatirlatici 30\n Bu komutla iftar zamanlarından 30 dakika önce otomatik mesaj alırsınız.";
			String str5 = "5) /iftar_hatirlatici_kapat komutuyla ayarladığınız hatırlatıcıyı kapatabilirsiniz.";
			String str6 = "6) /imsak_hatirlatici_kapat komutuyla ayarladığınız hatırlatıcıyı kapatabilirsiniz.";

			String text = String.format("%s\n\n%s\n\n%s\n\n%s\n\n%s\n\n%s", str1, str2, str3, str4, str5, str6);

			return text;
		}

		if (command.startsWith("/sehir_kaydet")) {
			String[] split = command.split(" ");
			if (split.length != 2) {
				return "Şehir ismi algılanamadı. Lütfen /sehir_kaydet komutu sonrasında boşluk bırakarak şehir ismini yazınız";
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

			service.setCityForUser(userId, cityName);

			return clearTurkishChars(cityName.toUpperCase()) + " bulunduğunuz şehir olarak sisteme kaydedildi.";
		}

		if (command.equals("/ogren")) {

			String cityName = user.getCity();

			if (cityName == null)
				return "Henüz şehirinizi kaydetmemişsiniz. \n/sehir_kaydet komutu ile şehirinizi kaydedebilirsiniz. Bilgi için /help";

			return service.askForCity(cityName);
		}

		if (command.startsWith("/iftar_hatirlatici_kapat")) {
			if (user.isIftarReminderEnabled()) {
				service.disableIftarReminderForUser(userId);
				return "İftar hatırlatıcınız kapatıldı";
			} else {
				return "İftar hatırlatıcınız zaten kapalı";
			}
		}

		if (command.startsWith("/imsak_hatirlatici_kapat")) {
			if (user.isImsakReminderEnabled()) {
				service.disableImsakReminderForUser(userId);
				return "İmsak hatırlatıcınız kapatıldı";
			} else {
				return "İmsak hatırlatıcınız zaten kapalı";
			}
		}

		if (command.startsWith("/iftar_hatirlatici")) {

			if (user.getCity() == null) {
				return "Henüz şehirinizi kaydetmemişsiniz. \n/sehir_kaydet komutu ile şehirinizi kaydedebilirsiniz. Bilgi için /help";
			}

			String[] split = command.split(" ");
			if (split.length != 2) {
				return "Zaman algılanamadı. Lütfen /iftar_hatirlatici komutu sonrasında boşluk bırakarak iftardan kaç dakika önce hatırlatıcı mesaj almak istediğinizi sayıyla yazınız.Ör:\n\n /iftar_hatirlatici 30";
			}

			String str = split[1];

			try {
				int reminderTime = getReminderTime(str);

				if (reminderTime > 0) {
					service.setIftarReminderForUser(userId, reminderTime);
					return String.format(
							"İftarlardan %s dakika önce bildirim alacaksınız. Hatırlatıcıyı kapatmak için /iftar_hatirlatici_kapat komutunu kullanabilirsiniz",
							reminderTime);
				} else {
					return "Lütfen pozitif bir sayı giriniz";
				}
			} catch (Exception e) {
				return "Lütfen komuttan sonra bir boşluk bırakarak istediğiniz değeri sayı olarak giriniz";
			}
		}

		if (command.startsWith("/imsak_hatirlatici")) {

			if (user.getCity() == null) {
				return "Henüz şehirinizi kaydetmemişsiniz. \n/sehir_kaydet komutu ile şehirinizi kaydedebilirsiniz. Bilgi için /help";
			}

			String[] split = command.split(" ");
			if (split.length != 2) {
				return "Zaman algılanamadı. Lütfen /imsak_hatirlatici komutu sonrasında boşluk bırakarak imsaktan kaç dakika önce hatırlatıcı mesaj almak istediğinizi sayıyla yazınız.Ör:\n\n /imsak_hatirlatici 30";
			}

			String str = split[1];

			try {
				int reminderTime = getReminderTime(str);

				if (reminderTime > 0) {
					service.setImsakReminderForUser(userId, reminderTime);
					return String.format(
							"İmsaklardan %s dakika önce bildirim alacaksınız. Hatırlatıcıyı kapatmak için /imsak_hatirlatici_kapat komutunu kullanabilirsiniz",
							reminderTime);
				} else {
					return "Lütfen pozitif bir sayı giriniz";
				}
			} catch (Exception e) {
				return "Lütfen komuttan sonra bir boşluk bırakarak istediğiniz değeri sayı olarak giriniz";
			}
		}

		// Bir şehir ismi girildiğinde burası çalışır

		String cityName;
		try {

			cityName = checkCityId(command);
		} catch (NumberFormatException e) {
			cityName = checkCityName(command);
		}

		if (cityName != null) {
			return clearTurkishChars(cityName.toUpperCase()) + "\n" + service.askForCity(cityName);
		} else {
			return command + " isimli şehir bulunamadı.";
		}
	}

	private int getReminderTime(String str) {
		return Integer.parseInt(str);
	}

	private String checkCityName(String cityName) {
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

	private String checkCityId(String cityName) {
		int cityId = Integer.parseInt(cityName) - 1;
		if (0 < cityId && cityId < 82)
			return clearTurkishChars(cities.get(cityId).toLowerCase());
		return null;
	}

	// https://gist.github.com/onuryilmaz/6034569
	private static String clearTurkishChars(String str) {
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
}
