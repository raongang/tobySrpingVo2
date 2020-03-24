package springbook.learningtest.spring.ioc.resource;

import org.springframework.stereotype.Component;

@Component("printer")
public class ConsolePrinter implements Printer{

	@Override
	public void print(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}
}
