package springbook.learningtest.spring.ioc.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleConfig {
	@Autowired Hello hello;
	
	@Bean Hello hello() {
		return new Hello();
	}
}

class Hello{
	//빈 오브젝트가 만들어진 뒤에 init이 실행됨.
	@PostConstruct
	public void init() {
		System.out.println("Init");
	}
	
	public void sayHello() { System.out.println("Hello ~!~!"); }
}