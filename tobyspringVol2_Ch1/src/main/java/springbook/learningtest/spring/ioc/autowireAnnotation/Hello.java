package springbook.learningtest.spring.ioc.autowireAnnotation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//pojo Hello는 Printer Interface에 의존.

@Component
public class Hello {

	@Value("Spring")
	String name;
	
	/* @Autowired - 이름 대신 필드나 프로퍼티 타입을 이용해서 후보 빈을 찾는다. 
	 * XML의 자동 와이어링(byType)과 비슷. 
	 * 
	 * 필드의 타입이 Printer이므로 현재 등록된 bean중에서(autowireAnnotation.xml) 
	 * 
	 * (required=false)
	 *  -bean을 못 찾을 경우 error가 발생하는데 못 찾더라도 상관없다면 required 로 설정.
	 * 
	 */
	@Autowired(required=false)
	@Qualifier("stringPrinter") //type이 여러 개 일 경우 지정
	//@Qualifier("consolePrinter") //type이 여러 개 일 경우 지정
	private Printer printer;
	
	/*
	@Autowired
	public Hello(Printer printer) {
		System.out.println("Hello Constructor Enter");
		this.printer = printer;
	}*/
	
	public String sayHello() {
		return "Hello " + name; //->프로퍼티로 DI받은 이름을 이용
	}
	
	public void print() {
		//DI에 의존오브젝트로 제공받은 Printer 타입의 오브젝트에게 출력작업 위임.
		//구체적으로 어떤 방식으로 출력하는지는 상관하지 않는다. 또한 어떤 방식으로 출력하도록 변경해도 Hello의 코드는 수정할 필요없음.
		this.printer.print(sayHello()); 
	}

	public void setName(String name) { //string값 DI
		this.name = name;
	}

	//@Autowired
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	public Printer getPrinter() {
		return printer;
	}
	
}
