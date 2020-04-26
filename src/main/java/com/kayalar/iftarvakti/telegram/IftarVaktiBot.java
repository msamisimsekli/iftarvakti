package com.kayalar.iftarvakti.telegram;

import com.kayalar.iftarvakti.service.IftarVaktiService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

public class IftarVaktiBot extends TelegramLongPollingBot {

    private List<String> cities = Arrays.asList(new String[]{"ADANA","ADIYAMAN","AFYON","AGRI","AMASYA","ANKARA","ANTALYA","ARTVIN","AYDIN","BALIKESIR","BILECIK","BINGOL","BITLIS","BOLU","BURDUR","BURSA","CANAKKALE","CANKIRI","CORUM","DENIZLI","DIYARBAKIR","EDIRNE","ELAZIG","ERZINCAN","ERZURUM","ESKISEHIR","GAZIANTEP","GIRESUN","GUMUSHANE","HAKKARI","HATAY","ISPARTA","MERSIN","ISTANBUL","IZMIR","KARS","KASTAMONU","KAYSERI","KIRKLARELI","KIRSEHIR","KOCAELI","KONYA","KUTAHYA","MALATYA","MANISA","KAHRAMANMARAS","MARDIN","MUGLA","MUS","NEVSEHIR","NIGDE","ORDU","RIZE","SAKARYA","SAMSUN","SIIRT","SINOP","SIVAS","TEKIRDAG","TOKAT","TRABZON","TUNCELI","SANLIURFA","USAK","VAN","YOZGAT","ZONGULDAK","AKSARAY","BAYBURT","KARAMAN","KIRIKKALE","BATMAN","SIRNAK","BARTIN","ARDAHAN","IGDIR","YALOVA","KARABUK","KILIS","OSMANIYE","DUZCE"});
    IftarVaktiService service = new IftarVaktiService();

    public void onUpdateReceived(Update update) {

        //System.out.println(update.getMessage().getText());
        //System.out.println(update.getMessage().getDate());
        //System.out.println(update.getMessage().getFrom());

        String command = update.getMessage().getText();

        SendMessage message = new SendMessage();

        System.out.println(update.getMessage().getFrom().getUserName() + " : " + command);

        //TODO: Commandlist db conf olup cache'de tutulacak
        if(command.equals("/start")){

            message.setText("Hoşgeldiniz! /help yazarak bot hakkında bilgi alabilirsiniz.");
        }

        else if (command.equals("/help")){

            message.setText("İftar bilgisi öğrenmek istediğiniz şehrin adını yazıp gönderebilirsiniz. Ör:Ankara");
        }
        else{

            System.out.println(checkCity(command));

            String reply = service.askForCity(checkCity(command));

            message.setText(checkCity(reply));

        }

        System.out.println(update.toString());
        message.setChatId(update.getMessage().getChatId());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    public String checkCity(String cityName){
        for(String city : cities){

            String cityNameEn = clearTurkishChars(cityName.toUpperCase());

            if (cityNameEn.equals(city)){
                System.out.println(cityNameEn);
                return cityNameEn;
            }
        }

        return cityName + " isimli şehir bulunamadı.";
    }

    //https://gist.github.com/onuryilmaz/6034569
    public static String clearTurkishChars(String str) {
        String ret = str;
        char[] turkishChars = new char[] {0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F, 0x11E};
        char[] englishChars = new char[] {'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
        for (int i = 0; i < turkishChars.length; i++) {
            ret = ret.replaceAll(new String(new char[]{turkishChars[i]}), new String(new char[]{englishChars[i]}));
        }
        return ret;
    }

    public String getBotUsername() {
        return "your-bot-userName";
    }

    public String getBotToken() {
        return "your-bot-token";

    }
}

