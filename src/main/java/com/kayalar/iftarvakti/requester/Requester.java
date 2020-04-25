package com.kayalar.iftarvakti.requester;

import java.io.IOException;
import java.net.URISyntaxException;

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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kayalar.iftarvakti.model.DayInfo;

public class Requester {

	CloseableHttpClient httpClient;
	JsonParser jsonParser;

	public Requester() {
		httpClient = HttpClients.createDefault();
		jsonParser = new JsonParser();
	}

	public DayInfo request(String sehir, String tarih)
			throws ClientProtocolException, IOException, URISyntaxException, HttpException, NumberFormatException {

		String ulke = "TURKIYE";
		String format = "json";

		URIBuilder builder = new URIBuilder("http://hocaokudumu.com/namazsaati");
		builder.setParameter("Ulke", ulke).setParameter("Sehir", sehir).setParameter("Tarih", tarih)
				.setParameter("format", format);

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

			// return it as a String
			String result = EntityUtils.toString(entity);
			JsonObject json = jsonParser.parse(result).getAsJsonObject();

			System.out.println(result);

			String imsak = json.get("Imsak").getAsString();
			String sabah = json.get("Gunes").getAsString();
			String ogle = json.get("Oglen").getAsString();
			String ikindi = json.get("Ikindi").getAsString();
			String aksam = json.get("Aksam").getAsString();
			String yatsi = json.get("Yatsi").getAsString();

			int imsakHour = Integer.parseInt(imsak.substring(0, 2));
			int sabahHour = Integer.parseInt(sabah.substring(0, 2));
			int ogleHour = Integer.parseInt(ogle.substring(0, 2));
			int ikindiHour = Integer.parseInt(ikindi.substring(0, 2));
			int aksamHour = Integer.parseInt(aksam.substring(0, 2));
			int yatsiHour = Integer.parseInt(yatsi.substring(0, 2));

			int imsakMinute = Integer.parseInt(imsak.substring(4));
			int sabahMinute = Integer.parseInt(sabah.substring(4));
			int ogleMinute = Integer.parseInt(ogle.substring(4));
			int ikindiMinute = Integer.parseInt(ikindi.substring(4));
			int aksamMinute = Integer.parseInt(aksam.substring(4));
			int yatsiMinute = Integer.parseInt(yatsi.substring(4));

			DayInfo dayInfo = new DayInfo(imsakHour, sabahHour, ogleHour, ikindiHour, aksamHour, yatsiHour, imsakMinute,
					sabahMinute, ogleMinute, ikindiMinute, aksamMinute, yatsiMinute);

			return dayInfo;
		}
	}
}
