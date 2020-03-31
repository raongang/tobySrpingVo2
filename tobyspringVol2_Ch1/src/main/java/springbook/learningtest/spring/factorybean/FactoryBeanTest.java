package springbook.learningtest.spring.factorybean;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
- 스프링은 클래스 정보를 가지고 디폴트 생성자를 통해 오브젝트를 만드는 방법 외에도 빈을 만들수 있는 방법이 있음.
- 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 특별한 빈.
- FactoryBean 인터페이스를 구현하고 이를 스프링 di로 등록하면 Factory Bean으로 동작.
*/


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FactoryBeanTest {
 
	@Autowired
	ApplicationContext context;
	
	@Test
	public void getMessageFromFactoryBean() {
		Object message = context.getBean("message");
		//assertThat(message,is(Message.class)); //type확인
		
		System.out.println("Text : " + ((Message)message).getText());
		assertThat(((Message)message).getText(),is("Factory Bean"));
		
		//Factory Bean이 만들어주는 빈 자체가 아니라, FactoryBean자체를 가져오고 싶을때는 & 이용.
		Object factory = context.getBean("&message");
		//assertThat(factory,is(MessageFactoryBean.class));
	}
	
}





