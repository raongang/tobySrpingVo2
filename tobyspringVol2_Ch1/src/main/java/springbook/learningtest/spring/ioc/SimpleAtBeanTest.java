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
		 *                                     - 등록할 빈 클래스를 직접 지정 할 수 있음.
		 */
		AbstractApplicationContext ac = new AnnotationConfigApplicationContext(BeanA.class,BeanB.class);
			
		BeanA beanA = ac.getBean(BeanA.class); //type
		assertThat(beanA, is(notNullValue()));
		
		BeanB beanB = ac.getBean(BeanB.class);
		assertThat(beanB,is(notNullValue()));
		
		//@Autowired 설정 있어야 함.
		
		
		assertThat(beanA.beanB, is(notNullValue()));

	}//test
	/*
		bean으로 등록할 애노테이션 설정을 가진 클래스를 static 클래스로 정의한다.
		빈 스캔을 할 것이 아니기 때문에, 자동등록되는 클래스라 하더라도 @Component를 붙여 줄 필요는 없다.
		빈의 이름을 지정할 필요가 있다면 @Component("beanname")이나 @Named("beanname") 사용.
	*/
	
	/**
	 *   ※ 필드에 @Autowired가 붙어 있음
	 *   - 필드나 프로퍼티 타입으로 di를 해주는데, 현재 등록된 Bean중에서 BeanB Type에 대입이 가능한 빈을 찾는다.
	 *   - AnnotationConfigApplicationContext에서 BeanB를 등록하였기 때문에, BeanB가 @Autowired에 의해 di됨.
	 *  
	 *
	 */
	private static class BeanA{ @Autowired BeanB beanB; }	
	private static class BeanB{}
	
	

}
