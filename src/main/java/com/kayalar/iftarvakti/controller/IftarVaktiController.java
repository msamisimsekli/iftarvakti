package com.kayalar.iftarvakti.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayalar.iftarvakti.requester.ApiUser;

@RestController
public class IftarVaktiController {

	ApiUser apiUser = new ApiUser();

	@RequestMapping("/")
	public String getDay() throws ClientProtocolException, IOException {
		apiUser.request();
		return "";
	}

}
