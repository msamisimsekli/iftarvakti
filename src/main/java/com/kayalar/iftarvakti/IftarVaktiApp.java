package com.kayalar.iftarvakti;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import com.kayalar.iftarvakti.requester.Requester;

public class IftarVaktiApp {

	public static void main(String[] args)
			throws NumberFormatException, ClientProtocolException, IOException, URISyntaxException, HttpException {
		Requester requester = new Requester();

		System.out.println(requester.request("ANKARA", "25.04.2020"));
	}
}
