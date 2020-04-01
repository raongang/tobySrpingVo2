package springbook.learningtest.spring.ioc.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class SimpleConfigTest {

	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	@Test
	public void configTest() {
		System.out.println("basePath : " + basePath);
		ApplicationContext context = new GenericXmlApplicationContext(basePath + "simpleConfig.xml");
		
		SimpleConfig sc = context.getBean(SimpleConfig.class);
		
		sc.hello.sayHello();
		
	}
}
