package springbook.learningtest.spring.ioc.autowireAnnotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//pojo

@Component
@Qualifier("stringPrinter") //type이 여러 개 일 경우 지정
public class StringPrinter implements Printer{

	private StringBuffer buffer = new StringBuffer();
	
	@Override
	public void print(String message) {
		// TODO Auto-generated method stub
		this.buffer.append(message);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.buffer.toString();
	}
	
	

}
