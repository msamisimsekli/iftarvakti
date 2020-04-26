package com.kayalar.iftarvakti.telegram;

import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.service.IftarVaktiService;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

public class IftarVaktiBot extends TelegramLongPollingBot {

	private List<String> cities = Arrays.asList(new String[] { "ADANA", "ADIYAMAN", "AFYON", "AĞRI", "AMASYA", "ANKARA",
			"ANTALYA", "ARTVİN", "AYDIN", "BALIKESİR", "BİLECİK", "BİNGOL", "BİTLİS", "BOLU", "BURDUR", "BURSA",
			"ÇANAKKALE", "ÇANKIRI", "ÇORUM", "DENİZLİ", "DİYARBAKIR", "EDİRNE", "ELAZIĞ", "ERZİNCAN", "ERZURUM",
			"ESKİSEHİR", "GAZİANTEP", "GİRESUN", "GÜMÜŞHANE", "HAKKARİ", "HATAY", "ISPARTA", "MERSİN", "İSTANBUL",
			"İZMİR", "KARS", "KASTAMONU", "KAYSERİ", "KIRKLARELİ", "KIRŞEHİR", "KOCAELİ", "KONYA", "KÜTAHYA", "MALATYA",
			"MANİSA", "KAHRAMANMARAŞ", "MARDİN", "MUĞLA", "MUŞ", "NEVŞEHİR", "NİĞDE", "ORDU", "RİZE", "SAKARYA",
			"SAMSUN", "SİİRT", "SİNOP", "SİVAS", "TEKİRDAĞ", "TOKAT", "TRABZON", "TUNCELİ", "ŞANLIURFA", "UŞAK", "VAN",
			"YOZGAT", "ZONGULDAK", "AKSARAY", "BAYBURT", "KARAMAN", "KIRIKKALE", "BATMAN", "ŞIRNAK", "BARTIN",
			"ARDAHAN", "IĞDIR", "YALOVA", "KARABÜK", "KİLİS", "OSMANİYE", "DÜZCE" });

	IftarVaktiService service;
	Configurations config;

	public IftarVaktiBot(Configurations config) {
		service = new IftarVaktiService();
		this.config = config;
	}

	public void onUpdateReceived(Update update) {

		// System.out.println(update.getMessage().getText());
		// System.out.println(update.getMessage().getDate());
		// System.out.println(update.getMessage().getFrom());

		String command = update.getMessage().getText();

		SendMessage message = new SendMessage();

		System.out.println(String.format("User=%s sent command=%s", update.getMessage().getFrom().getId(), command));

		// TODO: Commandlist db conf olup cache'de tutulacak
		if (command.equals("/start")) {

			message.setText("Hoşgeldiniz! /help yazarak bot hakkında bilgi alabilirsiniz.");
			return;
		}

		if (command.equals("/help")) {

			message.setText("İftar bilgisini öğrenmek istediğiniz şehrin adını yazıp gönderebilirsiniz. Ör:Ankara");
			return;
		}

		String reply;

		String cityName = checkCity(command);

		if (cityName != null) {
			System.out.println(cityName);
			reply = cityName + "\n" + service.askForCity(cityName);
		} else {
			reply = command + " isimli şehir bulunamadı.";
		}

		message.setText(reply);

		System.out.println(update.toString());
		message.setChatId(update.getMessage().getChatId());

		try

		{
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public String checkCity(String cityName) {
		for (String city : cities) {
			String cityNameUp = cityName.toUpperCase();

			SimilarityStrategy strategy = new JaroWinklerStrategy();
			StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
			double score = service.score(city, cityNameUp);

			if (city.length() == cityNameUp.length() && score > 0.75) {
				System.out.println(String.format("Score=%s", score));
				return city;
			}
		}
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
}
