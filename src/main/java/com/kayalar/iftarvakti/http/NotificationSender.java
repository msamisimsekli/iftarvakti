package com.kayalar.iftarvakti.http;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class NotificationSender {

	private static final String TELEGRAM_ADDRESS = "https://api.telegram.org";

	private String token;

	public NotificationSender(String token) {
		this.token = token;
	}

	public void sendNotification(Long chatId, String message)
			throws URISyntaxException, ClientProtocolException, IOException, HttpException {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		URIBuilder builder = new URIBuilder(TELEGRAM_ADDRESS);
		builder.setPath(String.format("/bot%s/sendMessage", token));
		builder.setParameter("chat_id", String.valueOf(chatId));
		builder.setParameter("text", message);

		HttpGet request = new HttpGet(builder.build());

		// add request headers
		request.addHeader("content-type", "application/json");

		CloseableHttpResponse response = httpClient.execute(request);

		if (response.getStatusLine().getStatusCode() != 200) {
			System.err.println(String.format("Could not send message to user: %s", response));
		}

		httpClient.close();
	}
}
