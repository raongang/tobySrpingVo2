package springbook.learningtest.spring.ioc.bean;

public class ConsoleShow implements Show{

	@Override
	public void show(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
		
	}

}
