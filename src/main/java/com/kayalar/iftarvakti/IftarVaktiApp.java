package com.kayalar.iftarvakti;

import com.kayalar.iftarvakti.model.RemainingTime;
import com.kayalar.iftarvakti.service.IftarVaktiService;

public class IftarVaktiApp {

	public static void main(String[] args) {
		IftarVaktiService service = new IftarVaktiService();

		RemainingTime rt = service.askForCity("ISTANBUL");

		System.out.println(rt);
	}
}
