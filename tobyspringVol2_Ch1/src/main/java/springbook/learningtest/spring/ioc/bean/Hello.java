package springbook.learningtest.spring.ioc.bean;

//pojo
// Hello는 Printer Interface에 의존.
public class Hello {
	
	String name;
	Printer printer;
	
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

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

}
