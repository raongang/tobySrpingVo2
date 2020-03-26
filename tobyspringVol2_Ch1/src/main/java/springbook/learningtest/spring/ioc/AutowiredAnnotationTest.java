package springbook.learningtest.spring.ioc;


import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;


import springbook.learningtest.spring.ioc.autowireAnnotation.Hello;
 
public class AutowiredAnnotationTest {
	
	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	@Test
	public void autowireAnnotation() {
		ApplicationContext ac = new GenericXmlApplicationContext(basePath + "autowireAnnotation.xml");
		
		//"hello" -> bean이름, Hello.class -> POJO CLASS
		//Hello hello = ac.getBean("hello",Hello.class);
		
		/*
		 *  만약, 특정 타입의 빈이 하나만 존재한다면, 
		 */
		Hello hello = ac.getBean(Hello.class);
		
		hello.print();
		assertThat(hello.getPrinter().toString(),is("Hello Spring"));
	}
	
	
	/**
	 *  애노테이션 설정을 위한 단위테스트 모듈
	 *    - 매번 XML에 설정이나 스캐닝을 하기 위해서는 매번 설정 및 패키지를 생성해서 만들어야 하는 불편함이 있다.
	 *    - Junit 테스트안에서 바로 테스트 할수 있는 샘플을 이용하는게 편함.
	 *    - AnnotationConfigApplicationContext : 빈 스캐너를 내장하고 있는 appalication context 구현 클래스, 등록할 클래스를 직접 지정할 수 있음.
	 */
	@Test
	public void atQualifier() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(QClient.class, ConsolePrinterA.class, StringPrinterA.class);
		QClient qclient = ac.getBean(QClient.class);
		
		//JUnit3에서만 동작.  
		//assertThat(qclient.service,is(QServiceA.class));
		//JUnit4에서는 아래와 같이 사용.
	    assertThat(qclient.printerA, is(instanceOf(ConsolePrinterA.class)));
	    qclient.print();
	    
	}
	static class QClient {
		@Autowired @Qualifier("main") PrinterA printerA;
		
		public void print() {
			//DI에 의존오브젝트로 제공받은 Printer 타입의 오브젝트에게 출력작업 위임.
			//구체적으로 어떤 방식으로 출력하는지는 상관하지 않는다. 또한 어떤 방식으로 출력하도록 변경해도 Hello의 코드는 수정할 필요없음.
			this.printerA.print("테스트"); 
		}
		
	}
	interface PrinterA { void print(String message); }
	
	@Qualifier("main")
	static class ConsolePrinterA implements PrinterA {
		@Override
		public void print(String message) {
			// TODO Auto-generated method stub
			System.out.println(message);
		}
		
	}//QServiceA
	
	static class StringPrinterA implements PrinterA {
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
	}//QServiceB
	
	
}
