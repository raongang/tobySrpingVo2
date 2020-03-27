package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class ValueAnnotationTest {

	@Test
	public void valueInject() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanSP.class,ConfigSP.class, DatabasePropertyPlaceHolder.class);
		BeanSP beansp = ac.getBean(BeanSP.class);
		System.out.println(beansp.name);
		System.out.println(beansp.username);
		System.out.println(beansp.osname);
		
		assertThat(beansp.name, is("Windows 7")); 
		assertThat(beansp.username, is("Spring"));
		
		assertThat(beansp.hello.name,is("Spring"));
		
	}
	
	static class BeanSP{
		@Value("#{systemProperties['os.name']}") String name;
		@Value("${database.username}") String username;
		@Value("${os.name}") String osname;
		@Autowired Hello hello;
	}
	
	static class ConfigSP{
		@Bean public Hello hello(@Value("${database.username}")String username) { //bean등록
			Hello hello = new Hello();
			hello.name = username;
			return hello;
		}
	}
	
	static class Hello{ String name; }
	
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
	
	
	//스프링에서 제공되는 타입변환기 예제.
	//@Value , XML의 value attribute는 String외 타입에 대해서는 타입을 변경하기 위한 두 가지 종류의 타입변환 서비스를 제공한다.
	@Test
	public void propertyEditor() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanPE.class);
		
		BeanPE bean = ac.getBean(BeanPE.class);
		assertThat(bean.charset,is(Charset.forName("UTF-8")));
		assertThat(bean.intarr, is(new int[] {1,2,3}));
		assertThat(bean.flag,is(true));
		assertThat(bean.rate,is(1.2));
		assertThat(bean.file.exists(),is(true));
	}
	
	static class BeanPE{
		@Value("UTF-8") Charset charset;
		@Value("1,2,3") int[] intarr; //배열주입																
		@Value("true") boolean flag;
		@Value("1.2") double rate;
		@Value("classpath:test-applicationContext.xml") File file;
	}
	
	//collection의 값 주입.
	@Test
	public void collectionInject() {
		
		
		
		ApplicationContext ac = new GenericXmlApplicationContext(new ClassPathResource("collection.xml",getClass()));
		
		BeanC bean = ac.getBean(BeanC.class);
		
		assertThat(bean.nameList.size(),is(3));
		assertThat(bean.nameList.get(0),is("Spring"));
		assertThat(bean.nameList.get(1),is("IoC"));
		assertThat(bean.nameList.get(2),is("DI"));
		
		assertThat(bean.nameSet.size(),is(3));
		
		assertThat(bean.ages.get("Kim"),is(30));
		assertThat(bean.ages.get("Lee"),is(35));
		assertThat(bean.ages.get("Ahn"),is(40));
		
		assertThat((String)bean.settings.get("username"),is("Spring"));
		assertThat((String)bean.settings.get("password"),is("Book"));
		
		assertThat(bean.beans.size(),is(2));
		
	}
	
	static class BeanC{
		List<String> nameList;
		Set<String> nameSet;
		Map<String,Integer> ages;
		Properties settings;
		List beans;
		
		public void setNameList(List<String> nameList) { this.nameList = nameList; }
		public void setNameSet(Set<String> nameSet) { this.nameSet = nameSet; }
		public void setAges(Map<String, Integer> ages) { this.ages = ages; }
		public void setSettings(Properties properties) { this.settings = properties; }
		public void setBeans(List beans) { this.beans = beans; }
		
	}
}
