package com.kayalar.iftarvakti;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import com.kayalar.iftarvakti.model.RemainingTime;
import com.kayalar.iftarvakti.service.IftarVaktiService;

public class IftarVaktiApp {

	public static void main(String[] args)
			throws NumberFormatException, ClientProtocolException, IOException, URISyntaxException, HttpException {
		IftarVaktiService service = new IftarVaktiService();

		RemainingTime rt = service.askForCity("ISTANBUL");

		System.out.println(rt);
	}
}
