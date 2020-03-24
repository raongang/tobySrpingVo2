package springbook.learningtest.spring.ioc.resource;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//pojo
// Hello는 Printer Interface에 의존.

//@Component는 기본적으로 class이름을 bean id로 사용.
@Component
public class Hello {
	
	@Value("Spring")
	String name;
																
	/* 참조할 bean의 이름을 지정한다. field injection.
	 *  @Component 선언시 기본적으로 class id를 bean id로 하므로 Printer 인터페이스를 구현하는 ConsolePrinter에 "printer"이름을 선언해줘야 한다.
	 *  @Resouce에서 name생략시 DI할 bean의 이름이 프로퍼티나 필드이름과 같다고 가정한다. 
	 */
	
	//@Resource(name="printer")
	@Resource
	private Printer printer;
	
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
	
	/* <property name="printer" ref="printer" />
	 * 
	 * (name="printer")는 참조할 빈의 이름을 printer로 설정하므로 bean id="printer" 를 참고
	 * ref="printer"와 같다고 생각하면 됨.
	 *  
	 *  여기서 property name="printer"에 해당하는 부분이 없는데, 이 부분은 setter를 보고 유추함.
	 * 
	@Resource(name="printer")
	
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}*/
    
}
