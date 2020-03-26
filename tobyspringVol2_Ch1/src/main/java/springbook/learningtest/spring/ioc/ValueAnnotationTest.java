package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

public class ValueAnnotationTest {

	@Test
	public void valueInject() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanSP.class,DatabasePropertyPlaceHolder.class);
		BeanSP beansp = ac.getBean(BeanSP.class);
		System.out.println(beansp.name);
		System.out.println(beansp.username);
		System.out.println(beansp.osname);
		
		assertThat(beansp.name, is("Windows XP")); 
		assertThat(beansp.username, is("Spring"));
		
	}
	
	
	
	static class BeanSP{
		@Value("#{systemProperties['os_name']}") String name; //null??
		@Value("${database.username}") String username;
		@Value("${os.name}") String osname;
	}
	
	static class ConfigSP{
		@Bean public Hello hello(@Value("${database.username}")String username) {
			Hello hello = new Hello();
			hello.name = username;
			return hello;
		}
	}
	
	static class Hello{
		String name;
	}
	
	/** WEB에서는 root-context.xml에 다음과 같이 선언
 		<context:property-placeholder location="classpath:/jdbc.properties"/>
	 */
	static class DatabasePropertyPlaceHolder extends PropertyPlaceholderConfigurer{
		public DatabasePropertyPlaceHolder() {
			//class springbook.learningtest.spring.ioc.ValueAnnotationTest$DatabasePropertyPlaceHolder
			System.out.println("getClass >> " + getClass());
			this.setLocation(new ClassPathResource("database.properties",getClass()));
		}
	}
}
