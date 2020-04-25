package com.kayalar.iftarvakti;

import com.kayalar.iftarvakti.service.IftarVaktiService;

public class IftarVaktiApp {

	public static void main(String[] args) {
		IftarVaktiService service = new IftarVaktiService();

		String rt = service.askForCity("İSTANBUL");

		System.out.println(rt);
	}
}
