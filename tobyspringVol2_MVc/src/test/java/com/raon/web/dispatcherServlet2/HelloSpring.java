package com.raon.web.dispatcherServlet2;

import org.springframework.stereotype.Component;

/* applicationContext create Test */

@Component
public class HelloSpring {
	public String sayHello(String name) {
		return "hello " + name;
		
	}
}
