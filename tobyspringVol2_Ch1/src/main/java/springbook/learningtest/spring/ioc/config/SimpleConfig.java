package springbook.learningtest.spring.ioc.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleConfig {

	@Autowired Hello hello;

	// @Bean이 붙은 메소드의 리턴오브젝트도 빈으로 등록해준다.
	@Bean Hello hello() { return new Hello();}

	public class Hello{
		//빈 오브젝트 생성이후 실행 
		@PostConstruct
		public void init() {
			System.out.println("Init");
		}
		
		public void sayHello() { System.out.println("Hello~~~"); }
		
		//얘는 테스트 어떻게 하지?
		@PreDestroy
		public void destroy() {
			System.out.println("destroy");
		}
	}
	
	
}
