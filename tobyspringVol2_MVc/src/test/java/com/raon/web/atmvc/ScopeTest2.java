package com.raon.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *   prototype bean의 dl전략
 *     - spring container인 application Context를 소스상에서 이용하는 것 외의 DL전략 방법
 *     
 *     - 
*/
public class ScopeTest2 {

	
	/* Spring에서 제공하는 ObjectFactory interface를 구현한 팩토리를 만들어주는 빈 클래스이용.
	 * Spring에서는 ObjectFactoryCreatingFactoryBean이라는 ObjectFactory Interface를 구현한 클래스를 제공.
	 */
	
//  웹에서 사용법
//	<bean id="serviceRequestFactory" class="org.springframework.factory.config.ObjectFactoryCreatingFactoryBean">
//		<property name="targetBeanName" value="serviceRequest" />     팩토리메소드에서 getBean으로 가져올 빈의 이름을 넣는다.
//	</bean>
	
	@Test
	public void objectFactory() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ObjectFactoryConfig.class);
		
		ObjectFactory<PrototypeBean> factoryBeanFactory = ac.getBean("prototypeBeanFactory", ObjectFactory.class);
		Set<PrototypeBean> bean = new HashSet<PrototypeBean>();
		
		for(int i=1; i<=4; i++) {
			bean.add(factoryBeanFactory.getObject());
			assertThat(bean.size(), is(i));
		}
	}

	@Component("prototypeBean")
	@Scope("prototype")
	static class PrototypeBean{}
	
	@Configuration
	static class ObjectFactoryConfig{
		@Bean 
		public ObjectFactoryCreatingFactoryBean prototypeBeanFactory() {
			ObjectFactoryCreatingFactoryBean factoryBean = new ObjectFactoryCreatingFactoryBean();
			factoryBean.setTargetBeanName("prototypeBean");
			return factoryBean;
		}
	}
	
	/**
	 * ====================================================================================================
	 *   Provider<T> 예제.
	 */
	
	@Test
	public void provideryTest() {
		
	}
}
