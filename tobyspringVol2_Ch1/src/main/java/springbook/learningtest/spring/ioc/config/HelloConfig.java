package springbook.learningtest.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

//@Bean 메소드의 의존관계와 싱글톤 
//자바코드로 표현되는 메타정보.
//자동인식 빈의 대상이 되므로 XML이 필요없다.
@Configuration
public class HelloConfig {
	
	@Bean
	public Hello hello() {
		Hello hello = new Hello();
		hello.setName("Spring");
		hello.setPrinter(printer()); //DI
		return hello;
	}
	
	@Bean 
	public Printer printer() {
		return new StringPrinter(); //매번 다른 오브젝트가 생성되는것이 아닌 싱글톤에 의해 동일한 빈 오브젝트가 생성됨.
	}
	
	@Bean
	public Hello hello2() {
		Hello hello = new Hello();
		hello.setName("Spring2");
		hello.setPrinter(printer()); //DI
		return hello;
	}
	

}
