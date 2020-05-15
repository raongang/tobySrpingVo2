package com.raon.spring3.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class ScopeTest {
	
	/*
	 *  싱글톤 스코프는 컨텍스트당 한개의 오브젝트만 만들어진다.  
	 *  싱글톤으로 설정된 bean은 DI이든 DL이든 상관없이 매번 같은 오브젝트가 만들어진다.
	 *  AnnotationConfigApplicationContext - 빈 스캐너를 내장하고 있는 appalication context 구현 클래스.
	 *                                     - 등록할 빈 클래스를 직접 지정 할 수 있음.
	*/
	@Test
	public void singletoneScope() {
		
		ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
		
		//set은 중복을 허용하지 않으므로 같은 오브젝트는 여러 번 추가해도 한개만 남는다.
		Set<SingletonBean> beans = new HashSet<SingletonBean>();
		
		//DL에서 싱글톤 확인
		beans.add(ac.getBean(SingletonBean.class));
		beans.add(ac.getBean(SingletonBean.class));
		assertThat(beans.size(),is(1));
		
		//DI에서 싱글톤 확인
		beans.add(ac.getBean(SingletonClientBean.class).bean1);
		beans.add(ac.getBean(SingletonClientBean.class).bean2);
		assertThat(beans.size(),is(1));
	}
	
	//싱글톤 스코프 빈. Scope 빈 메타정보의 디폴트 값은 "singleton"이기 때문에 별도의 스코프 설정은 필요없음.
	static class SingletonBean{}
	
	static class SingletonClientBean{
		//한번 이상 DI가 일어날수 있도록 두개의 DI용 프로퍼티를 선언.
		//AnnotationConfigApplicationContext 으로 등록할 빈을 직접 지정했고, @Autowired로 등록된 빈중에서 type이 같은것을 DI해준다.
		@Autowired SingletonBean bean1;
		@Autowired SingletonBean bean2;
	}
	
	//프로토 타입 스코프 - 컨테이너에게 빈을 요청할 때마다 매번 새로운 오브젝트를 생성.
	@Test
	public void prototypeScope() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);
		Set<PrototypeBean> bean = new HashSet<PrototypeBean>();
		
		//DL방식으로 컨테이너에 빈을 요청할때마다 새로운 빈 오브젝트가 생성.
		bean.add(ac.getBean(PrototypeBean.class));
		assertThat(bean.size(),is(1));
		bean.add(ac.getBean(PrototypeBean.class));
		assertThat(bean.size(),is(2));
		
		//프로토타입 빈을 DI할 때도 주입받는 프로퍼티마다 다른 오브젝트가 만들어지는 것을 확인. 
		bean.add(ac.getBean(PrototypeClientBean.class).bean1);
		assertThat(bean.size(),is(3));
		bean.add(ac.getBean(PrototypeClientBean.class).bean2);
		assertThat(bean.size(),is(4));
	}
	
	//애노테이션을 이용해 프로토타입 빈으로 설정.
	@Scope("prototype") 
	static class PrototypeBean{}
	
	static class PrototypeClientBean{
		@Autowired PrototypeBean bean1;
		@Autowired PrototypeBean bean2;
	}
	
	
}

