package springbook.learningtest.spring.ioc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import springbook.learningtest.spring.ioc.resource.Hello;

public class AnnotationConfigurationTest {

	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	@Test
	public void atResource() {
		
		System.out.println("basePath : " + basePath);
		
		ApplicationContext ac = new GenericXmlApplicationContext(basePath + "resource.xml");
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		//assertThat(hello.toString(), is("Hello Child"));
	}
}
