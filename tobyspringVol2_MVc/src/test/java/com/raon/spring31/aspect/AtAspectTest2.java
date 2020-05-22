package com.raon.spring31.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

public class AtAspectTest2 {
	
	@Test
	public void simple() {
		
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(HelloBean.class, HiBean.class, SimpleMornitoringAspect.class);
		ac.register(Client.class, HiBean.class);
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(ac);
		AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(ac);
		ac.refresh();
		
//		for(String name : ac.getBeanDefinitionNames()) {
//			System.out.println(name + "\t\t" + ac.getBean(name).getClass());
//		}
		
		ac.getBean(Client.class).dohello();
		ac.getBean(Client.class).dohibean();
			
		ac.getBean("hello", HelloBean.class).sayHello("Spring");
		ac.getBean("hello", HelloBean.class).add(1, 2);
		ac.getBean("hi", Hello.class).sayHello("Spring");
	}
	
	@Aspect 
	public static class SimpleMornitoringAspect {
		/*
		@Pointcut("this(com.raon.spring31.aspect.AtAspectTest2.HelloBean)")
		public void pointcutHb() {}
		
		//JoinPoint는 ProceedingJoinPoint의 super interface이지만, Join point 메소드 실행 지점에 대한 정보를 가져올순 있지만 타킷 오브젝트의 메소드를 실행하는 proceed()메소드는 없다.
		//@Before는 @Around처럼 타킷 오브젝트로 메소드로 전달되는 파라미터를 변경할수 없다.
		//JoinPoint를 통해 파라미터를 참조가능. 즉, 파라미터 자체를 변경할순 없어도 참조하는 오브젝트의 내용을 변경할수는 있다.
		
		@Before("pointcutHb()")
		public void hello(JoinPoint jp) {
			System.out.println(jp.getTarget().getClass());
			
			System.out.println(jp.getSignature().getDeclaringTypeName());
			System.out.println(jp.getSignature().getName());
			
			for(Object arg : jp.getArgs()) { System.out.println(arg); }
			
		}*/

		@Pointcut("execution(* *(..))")
		private void all() {}
	
		/*
		 *  @Around
		 *   - 프록시를 통해서 타깃 오브젝트의 메소드가 호출되는 전 과정을 모두 담을 수 있는 advice
		 *   - ProceedingJoinPoint 오브젝트의 proceed()메소드는 클라이언트가 보낸 파라미터를 그대로 사용해서 타깃 오브젝트의 메소드를 호출하고 그 결과를 리턴함
		 *   - 호출파라미터 변경 , 특정 예외처리, 리턴 값 조작도 가능.
		 */
		@Around("all()")
		public Object printParametersAndReturnVal(ProceedingJoinPoint pjp) throws Throwable {
			System.out.println("Class : " + pjp.getTarget().getClass());
			System.out.println("Method : " + pjp.getSignature().getName());
			System.out.print("Args : ");
			for(Object arg : pjp.getArgs()) System.out.print(arg + " / "); 
			
			//클라이언트가 보낸 파라미터를 그대로 사용해서 타킷 오브젝트의 메소드를 호출하고 그 결과를 return
			Object ret = pjp.proceed();
			
			System.out.println("\nReturn : " + ret);
			
			return ret;
		}
	}
	
	@Component("hello") static class HelloBean {
		public String sayHello(String name) {
			return "Hello " + name;
		}
		public int add(int a, int b) {
			return a+b;
		}
	}
	@Component("client") static class Client {
		@Autowired HiBean hiBean;
		@Autowired Hello hello;
		
		public void dohibean() {
			System.out.println(hiBean.sayHello("HiBean"));
		}
		public void dohello() {
			System.out.println(hiBean.sayHello("Hello"));
		}
	}
	
	interface Hello { String sayHello(String name);	}
	
	@Component("hi") 
	static class HiBean implements Hello {
		public String sayHello(String name) {
			return "Hi " + name;
		}
	}

}
