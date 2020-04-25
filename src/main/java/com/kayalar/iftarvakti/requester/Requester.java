package com.kayalar.iftarvakti.requester;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kayalar.iftarvakti.model.DayInfo;

public class Requester {

	private CloseableHttpClient httpClient;
	private JsonParser jsonParser;

	private static final String apiAdress = "http://hocaokudumu.com/namazsaatiliste";
	private static final String ulke = "TURKIYE";
	private static final String format = "json";

	public Requester() {
		httpClient = HttpClients.createDefault();
		jsonParser = new JsonParser();
	}

	public List<DayInfo> requestForList(String sehir)
			throws ClientProtocolException, IOException, URISyntaxException, HttpException, NumberFormatException {

		URIBuilder builder = new URIBuilder(apiAdress);
		builder.setParameter("Ulke", ulke).setParameter("Sehir", sehir).setParameter("format", format);

		HttpGet request = new HttpGet(builder.build());

		// add request headers
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new HttpException("Response was not successfull");
			}

			HttpEntity entity = response.getEntity();
//			Header headers = entity.getContentType();
//			System.out.println(headers);

			if (entity == null) {
				throw new HttpException("Response entitiy was null");
			}

			List<DayInfo> result = new ArrayList<DayInfo>();
			JsonArray jsonArray = jsonParser.parse(EntityUtils.toString(entity)).getAsJsonArray();

			for (JsonElement jsonElement : jsonArray) {
				JsonObject json = jsonElement.getAsJsonObject();

				String imsak = json.get("Imsak").getAsString();
				String sabah = json.get("Gunes").getAsString();
				String ogle = json.get("Oglen").getAsString();
				String ikindi = json.get("Ikindi").getAsString();
				String aksam = json.get("Aksam").getAsString();
				String yatsi = json.get("Yatsi").getAsString();
				String gun = json.get("Gun").getAsString();

				int day = Integer.parseInt(gun.substring(0, 2));
				int month = Integer.parseInt(gun.substring(3, 5));
				int year = Integer.parseInt(gun.substring(6));

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

				result.add(new DayInfo(day, month, year, imsakHour, sabahHour, ogleHour, ikindiHour, aksamHour,
						yatsiHour, imsakMinute, sabahMinute, ogleMinute, ikindiMinute, aksamMinute, yatsiMinute));

			}

			return result;
		}
	}
}
