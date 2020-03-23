package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import springbook.learningtest.spring.ioc.bean.AnnotatedHello;
import springbook.learningtest.spring.ioc.bean.AnnotatedHelloConfig;
import springbook.learningtest.spring.ioc.bean.AnnotatedHelloWithName;
import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

public class ApplicationContextTest {

	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	@Test
	public void print() {
		System.out.println(StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())));
	}
	
	@Test
	public void registerBean() {

		//IoC container 생성과 동시에 컨테이너로 동작. StaticApplicationContext -> ApplicationContext 구현 클래스.
		StaticApplicationContext ac = new StaticApplicationContext();
		//Hello 클래스를 hello1이라는 이름의 싱글톤 빈으로 컨테이너에 등록한다.
		ac.registerSingleton("hello1", Hello.class);
		
		//bean이름과 pojo class
		Hello hello1 = ac.getBean("hello1",Hello.class);
		//IoC 컨테이너가 등록한 빈을 생성했는지 확인하기 위해 빈을 요청하고 Null이 아닌지 체크
		assertThat(hello1,is(notNullValue()));
		
		//RootBeanDefinition ->  가장 기본적인 BeanDefinition interface의 구현 클래스
		//빈 메타정보를 담은 오브젝트를 생성한다. 빈 클래스는 Hello로 지정
		//<bean class="springbook.learningtest...Hello" /> 에 해당.
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		
		//빈의 name 프로퍼티에 들어값 지정 <property name="name" value="spring" /> 에 해당
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");

		ac.registerBeanDefinition("hello2", helloDef);
		Hello hello2 = ac.getBean("hello2",Hello.class);
		
		assertThat(hello2.sayHello(),is("Hello Spring"));
		
		//bean은 오브젝트 단위로 등록되고 만들어지기 때문에, 같은 클래스 타입이라고 할지라도 두개를 등록하면 서로 다른 빈 오브젝트가 생성된다.
		assertThat(hello1,is(not(hello2)));
		
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(),is(2));  
	}
	
	@Test
	public void registerBeanWithDependency() {
		StaticApplicationContext ac = new StaticApplicationContext();
		
		ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class)); //빈 등록
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		
		//id가 printer인 빈에 대한 레퍼런스를 프로퍼티로 등록
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		ac.registerBeanDefinition("hello", helloDef);
		
		Hello hello = ac.getBean("hello",Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
		
	}
	
	@Test
	public void genericApplicationContext() {
		GenericApplicationContext ac = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
		
		//XmlBeanDefinitionReader는 기본적으로 클래스패스로 정의된 리소스로부터 파일을 읽는다.
		reader.loadBeanDefinitions("springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		ac.refresh(); //모든 메타정보 등록이 완료됐으니 applcation container를 초기화하라는 명령
		
		Hello hello = ac.getBean("hello",Hello.class);
		hello.print();
		assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
	}
	
	//GenericApplicationContext 에서 XmlBeanDefinitionReader을 이용하던 것을 하나로 합침.
	@Test
	public void genericXmlAppicationContext() {
		
		//application context 생성과 동시에 XML파일을 읽어오고 초기화까지 수행한다.
		GenericApplicationContext ac = new GenericXmlApplicationContext(basePath + "genericApplicationContext.xml");
		Hello hello = ac.getBean("hello",Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
	}
	
	
	@Test(expected=BeanCreationException.class) //JUnit4 기능.
	public void createContextWithoutParent() {
		ApplicationContext child = new GenericXmlApplicationContext(basePath + "childContext.xml");
	}
	
	
	//컨텍스트 계층구조 테스트.
	@Test
	public void contextHierachy() {
		//부모 context 생성
		ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");
		
		//자식 context 생성
		GenericApplicationContext child = new GenericApplicationContext(parent);
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
		
		reader.loadBeanDefinitions(basePath + "childContext.xml");
		//XmlBeanDefinitionReader 를 사용할 경우 반드시  refresh()를 통해 초기화해야 한다.
		//이때, 자식 context에서 필요한 빈이 존재하지 않을 경우 부모 context에서 빈 검색을 요청하게 된다. 
		child.refresh(); 
		
		Printer printer = child.getBean("printer", Printer.class);
		assertThat(printer,is(notNullValue()));
		
		
		Hello hello = child.getBean("hello",Hello.class);
		assertThat(hello,is(notNullValue()));
		
		hello.print();
		assertThat(printer.toString(), is("Hello Child"));
	}

	//bean scanning test
	@Test
	public void SimpleBeanScanning() {
		
		/* @Component가 붙은 클래스를 스캔할 패키지를 넣어서 context를 만들어준다. 생성과 동시에 자동으로 스캔과 등록이 완료된다.
		 *  AnnotationConfigApplicationContext - 빈 스캐너를 내장하고 있는 appalication context 구현 클래스.
		 */
		ApplicationContext ctx = new AnnotationConfigApplicationContext("springbook.learningtest.spring.ioc.bean");
		
		//자동 등록되는 빈의 아이디는 클래스 이름의 첫 글자를 소문자로 바꿔서 사용한다.
		AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class); 
		
		assertThat(hello,is(notNullValue()));
	}

	//bean scanning test 
	@Test
	public void SimpleBeanScanningWithName() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("springbook.learningtest.spring.ioc.bean");
		AnnotatedHelloWithName hello = ctx.getBean("myAnnotatedHello", AnnotatedHelloWithName.class); 
		assertThat(hello,is(notNullValue()));
	}
	
	//java config test
	@Test
	public void configurationBean() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
		AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);
		assertThat(hello,is(notNullValue()));
		
		//설정을 담은 AnnotatedHelloConfig 자체도 Bean으로 등록됨.
		AnnotatedHelloConfig config = ctx.getBean("annotatedHelloConfig",AnnotatedHelloConfig.class);
		assertThat(config,is(notNullValue()));
		
		//나머지 메타 정보의 설정정보가 전부 default scope 적용 - 싱글톤
		assertThat(config.annotatedHello(),is(sameInstance(hello)));
	}
	
}
