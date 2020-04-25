package com.kayalar.iftarvakti.requester;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ApiUser {

	CloseableHttpClient httpClient;

	public ApiUser() {
		httpClient = HttpClients.createDefault();
	}

	public void request() throws ClientProtocolException, IOException {

		HttpGet request = new HttpGet(
				"http://hocaokudumu.com/namazsaati?Ulke=TURKIYE&Sehir=ANKARA&Tarih=24.04.2020&format=json");

		// add request headers
		request.addHeader("custom-key", "mkyong");
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

		try (CloseableHttpResponse response = httpClient.execute(request)) {

			// Get HttpResponse Status
			System.out.println(response.getStatusLine().toString());

			HttpEntity entity = response.getEntity();
			Header headers = entity.getContentType();
			System.out.println(headers);

			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				System.out.println(result);
			}

		}
	}
}
