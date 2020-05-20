package springbook.learningtest.spring.ioc.autowireAnnotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("consolePrinter") //type이 여러 개 일 경우 지정
public class ConsolePrinter implements Printer{

	@Override
	public void print(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}
	

}
