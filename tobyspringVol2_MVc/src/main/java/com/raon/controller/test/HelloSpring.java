package com.raon.controller.test;

import org.springframework.stereotype.Component;

@Component
public class HelloSpring {
	public String sayHello(String name) {
		return "hello " + name;
		
	}
}
