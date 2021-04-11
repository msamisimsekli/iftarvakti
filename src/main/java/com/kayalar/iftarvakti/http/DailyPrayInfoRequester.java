package com.kayalar.iftarvakti.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kayalar.iftarvakti.config.Configurations;
import com.kayalar.iftarvakti.model.DailyPrayTimes;

public class DailyPrayInfoRequester {

	private CloseableHttpClient httpClient;
	private JsonParser jsonParser;

	private String apiAdress;
	private String apiToken;

	public DailyPrayInfoRequester(Configurations config) {
		httpClient = HttpClients.createDefault();
		jsonParser = new JsonParser();

		apiAdress = config.getApiAddress();
		apiToken = config.getApiToken();
	}

	public DailyPrayTimes requestForCity(String sehir)
			throws ClientProtocolException, IOException, URISyntaxException, HttpException, NumberFormatException {

		URIBuilder builder = new URIBuilder(apiAdress);
		builder.setParameter("data.city", sehir);

		HttpGet request = new HttpGet(builder.build());

		// add request headers
		request.addHeader("content-type", "application/json");
		request.addHeader("authorization", apiToken);

		CloseableHttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new HttpException(String.format("Response was not successfull: %s", response.getStatusLine().getReasonPhrase()));
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new HttpException("Response entitiy was null");
		}

		String result = EntityUtils.toString(entity);

		JsonObject json = jsonParser.parse(result).getAsJsonObject();

		JsonArray jsonArray = json.get("result").getAsJsonArray();

		String imsak = jsonArray.get(0).getAsJsonObject().get("saat").getAsString();
		String sabah = jsonArray.get(1).getAsJsonObject().get("saat").getAsString();
		String ogle = jsonArray.get(2).getAsJsonObject().get("saat").getAsString();
		String ikindi = jsonArray.get(3).getAsJsonObject().get("saat").getAsString();
		String aksam = jsonArray.get(4).getAsJsonObject().get("saat").getAsString();
		String yatsi = jsonArray.get(5).getAsJsonObject().get("saat").getAsString();

		ZonedDateTime now = ZonedDateTime.now();
		int day = now.getDayOfMonth();
		int month = now.getMonthValue();
		int year = now.getYear();

		int imsakHour = Integer.parseInt(imsak.substring(0, 2));
		int sabahHour = Integer.parseInt(sabah.substring(0, 2));
		int ogleHour = Integer.parseInt(ogle.substring(0, 2));
		int ikindiHour = Integer.parseInt(ikindi.substring(0, 2));
		int aksamHour = Integer.parseInt(aksam.substring(0, 2));
		int yatsiHour = Integer.parseInt(yatsi.substring(0, 2));

		int imsakMinute = Integer.parseInt(imsak.substring(3));
		int sabahMinute = Integer.parseInt(sabah.substring(3));
		int ogleMinute = Integer.parseInt(ogle.substring(3));
		int ikindiMinute = Integer.parseInt(ikindi.substring(3));
		int aksamMinute = Integer.parseInt(aksam.substring(3));
		int yatsiMinute = Integer.parseInt(yatsi.substring(3));

		DailyPrayTimes dayInfo = new DailyPrayTimes(day, month, year, imsakHour, sabahHour, ogleHour, ikindiHour, aksamHour,
				yatsiHour, imsakMinute, sabahMinute, ogleMinute, ikindiMinute, aksamMinute, yatsiMinute);

		return dayInfo;
	}
}
