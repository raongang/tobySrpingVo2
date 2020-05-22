package com.raon.spring3.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*  테스트 컨텍스트 프레임워크
 *   - Junit을 이용한 test 
 *   - Junit4 -> 애노테이션방식으로 새롭게 만들어짐.
 *   - @Test가 붙은 메소드는 매번 테스트 클래스의 새로운 오브젝트를 생성하며 독립적으로 실행됨. 
 * */

//하나의 설정파일(.xml)로 만들어지는 application context를 공유.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("context.xml")
//@ContextConfiguration("{/test-applicationContext.xml", "/subContext.xml"})
//@ContextConfiguration 으로 선언시 클래스이름-context.xml이 붙은 파일이 default 파일이름으로 사용됨.

public class TestContextTest {
	
	/**
	 *   @Test 메소드의 개수만큼  TestContextTest class의 오브젝트가 반복적으로 만들어지지만,
	 *   context.xml을 설정파일로 하는 application context는 딱 하나 만들어짐.
	 */
	
	//type검색
	@Autowired BeanA a;
	//이름으로 검색
	@Resource BeanA beanA;
	
	BeanA b;
	BeanA c;
	
	
	@Autowired
	public void setBeanA(BeanA b) {
		this.b = b;
		
	}
	
	@Autowired
	public void init(BeanA c) {
		this.c = c;
	}

	@Test
	/* 컨텍스트는 한번만 만들어서 공유가 되는데, 어쩔 수 없이 컨텍스트의 빈 오브젝트를 조작하고 수정하는 작업을 하게 될경우
	 *  @DirtiesContext 사용
	 *  
	 *  @DirtiesContext
	 *    - @DirtiesContext이 붙은 테스트가 수행된 이후 스프링이 현 테스트 컨텍스트를 강제로 제거하여 공유되지 않게 함
	 *    - 이후 새로운 컨텍스트가 생성됨.
	 */
	@DirtiesContext
	public void test1() {
		assertThat(a,is(notNullValue()));
		assertThat(beanA,is(notNullValue()));
		assertThat(b, is(notNullValue()));
		assertThat(c, is(notNullValue()));		
	}
	
	@Test
	public void test2() {}
	
	static class BeanA{
		@PostConstruct 
		public void init() {
			System.out.println("A");
		}
	}
}
