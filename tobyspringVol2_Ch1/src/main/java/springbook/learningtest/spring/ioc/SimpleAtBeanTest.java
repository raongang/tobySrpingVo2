package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;



/**
 * 
 * @author raongang 
 *   애노테이션 설정을 위한 테스트 모듈.
 */
public class SimpleAtBeanTest {
	
	@Test
	public void simpleAutowired() {
		
		/*  AnnotationConfigApplicationContext - 빈 스캐너를 내장하고 있는 appalication context 구현 클래스.
		 *                                     - 등록한 빈 클래스를 직접 지정 할 수 있음.
		 */
		AbstractApplicationContext ac = new AnnotationConfigApplicationContext(BeanA.class,BeanB.class);
			
		BeanA beanA = ac.getBean(BeanA.class);
		assertThat(beanA, is(notNullValue()));
		
		BeanB beanB = ac.getBean(BeanB.class);
		assertThat(beanB,is(notNullValue()));
		
		//@Autowired 있어야 함.
		assertThat(beanA.beanB, is(notNullValue()));

	}//test
	
	private static class BeanA{ @Autowired BeanB beanB; }	
	private static class BeanB{}
	
	

}
