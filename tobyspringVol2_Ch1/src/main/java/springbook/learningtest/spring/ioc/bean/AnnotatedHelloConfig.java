package springbook.learningtest.spring.ioc.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//자바코드에 의한 bean 등록

@Configuration
public class AnnotatedHelloConfig {
	
	//@Bean이 붙은 메소드 하나가 하나의 빈을 정의. 메소드 이름이 등록되는 빈의 이름.	
	@Bean 
	public AnnotatedHello annotatedHello() {
		//return object를 container에서 활용.
		return new AnnotatedHello();
	}

}
