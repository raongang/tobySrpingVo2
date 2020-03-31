package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import springbook.learningtest.spring.ioc.autowired.Hello;

public class AutowiredXMLTest {

	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	//Auto wire - byName
	@Test
	public void autowireByName() {
		ApplicationContext ac = new GenericXmlApplicationContext(basePath + "autowire.xml");
		Hello hello = ac.getBean("hello",Hello.class);
		hello.print();
		assertThat(ac.getBean("printer").toString(),is("Hello spring"));
	}
	
	//Auto wire - byType
	@Test
	public void autowireByType() {
		ApplicationContext ac = new GenericXmlApplicationContext(basePath + "autowire.xml");
		Hello hello = ac.getBean("hello",Hello.class);
		hello.print();
		ac.getBean("consolePrinter").toString();
	}
	
}
