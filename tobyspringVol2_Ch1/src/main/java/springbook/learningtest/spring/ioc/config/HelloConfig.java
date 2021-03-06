package springbook.learningtest.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

//@Bean 메소드의 의존관계와 싱글톤 
//자바코드로 표현되는 메타정보.
//자동인식 빈의 대상이 되므로 XML이 필요없다.
//@Configuration 애노테이션은 자기자신도 빈으로 등록되면서 동시에 @Bean이 붙은 메소드의 리턴오브젝트도 빈으로 등록해준다.
@Configuration
public class HelloConfig {
	
	//@Bean이 붙은 메소드 하나가 하나의 빈을 정의. 메소드 이름이 등록되는 빈의 이름.	
	@Bean
	public Hello hello() {
		Hello hello = new Hello();
		hello.setName("Spring");
		hello.setPrinter(printer()); //수동 DI
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
