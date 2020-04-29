package com.kayalar.iftarvakti.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

	@RequestMapping(value = "/")
	public String dummyFunction() {
		System.out.println("Service called");
		return "HeyHey";
	}
}