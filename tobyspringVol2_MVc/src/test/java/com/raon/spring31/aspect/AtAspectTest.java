package com.raon.spring31.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AtAspectTest {

	/*
	 *  싱글톤 스코프는 컨텍스트당 한개의 오브젝트만 만들어진다.  
	 *  싱글톤으로 설정된 bean은 DI이든 DL이든 상관없이 매번 같은 오브젝트가 만들어진다.
	 *  AnnotationConfigApplicationContext - 빈 스캐너를 내장하고 있는 appalication context 구현 클래스.
	 *                                     - 등록할 빈 클래스를 직접 지정 할 수 있음.
	*/
	@Test
	public void simple() {
		
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(HelloImpl.class, HelloWorld.class, HelloAspect.class);
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(ac); //?
//		AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(ac);
		ac.refresh();
		
		Hello hello = ac.getBean(Hello.class);
		hello.makeHello("Spring");
		hello.makeHello("Spring", 2);
		int result = hello.add(1, 2);
		
		HelloAspect aspect = ac.getBean(HelloAspect.class);
		System.out.println(aspect.resultCombine);
		System.out.println(aspect.resultReturnString);
		System.out.println(aspect.resultHelloClass);
		System.out.println(aspect.resultCommon);
	}
	
	@Aspect 
	static class HelloAspect{
		List resultCombine = new ArrayList();		
		List resultReturnString = new ArrayList();		
		List resultHelloClass = new ArrayList();
		List resultCommon = new ArrayList();
		
		public void clear() { resultCombine.clear(); resultReturnString.clear(); resultHelloClass.clear();  resultCommon.clear(); }

		/*
		 *  메소드를 통해 정의되는 point cut의 이름은 메소드이름과 동일한 returnString().
		 *  private type
		 *  return type은 반드시 void
		 *  point cut  
		 *    └ 적용할 join point를 선별하는 것
		 *  join point 
		 *    └ advice로 정의된 부가기능을 적용할 수 있는 위치
		 *    └ 스프링에서는 프록시 방식의 AOP를 사용하기 때문에 join point는 메소드 실행지점.
		 *    └ point cut 설명에서 join point라고 하면 메소드를 가르킨다.
		 *    
		 *    execution 지시자
		 *      - return type, method, parameter 3가지는 필수 항목.
		 * */
		
		//@Pointcut("execution(String *(..))") private void returnString() {}
		
		/**/
		
		
		
		@Before("returnString()") //point cut 이름 
		public void callReturnString(JoinPoint jp) {
			System.out.println("jp.getSignature().getDeclaringTypeName() \t" + jp.getSignature().getDeclaringTypeName());
			System.out.println("jp.getSignature().getName() \t" + jp.getSignature().getName());
			for(Object arg : jp.getArgs()) {
			System.out.println("arg \t" + arg);
			}
		}
		
		
		
		/*
		 *    target object method가 실행되기전에 실행되는 advice.
		 *    JoinPoint - ProceedingJoinPoint의 super interface
		 *    
		 *    @target(an) 
		 *     - 포인트컷은 이름이 일치하는 메소드 파라미터의 타입정보를 참조하고, 어드바이스 메소드는 타킷 오브젝트의 애노테이션을 전달받는다.
		 *     - @target 지시자는 타킷 오브젝트에 특정 애노테이션이 부여된것을 선정한다.
		 * 
		 * */
		
		//@Before("@target(an)")
		//@Before("@target(com.raon.spring31.aspect.AtAspectTest)")
//		@Before("returnString()")
//		public void an(JoinPoint jp, Anno an){
//			System.out.println(an);
//		}
		
	}
	
	//사용자 정의 annotation
	/*
	@Target({ElementType.TYPE, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Anno{
		String value() default "a";
	}*/
	
	interface Hello{
		int add(int a, int b);
		String makeHello(String name);
		String makeHello(String name, int repeat);
	}
	
	//@Anno("b")
	static class HelloImpl implements Hello{

		@Override
		public int add(int a, int b) {
			return a+b; 
		}

		@Override
		public String makeHello(String name) {
			return "Hello " + name; 
		}

		@Override
		public String makeHello(String name, int repeat) {
			String names = "";
			while(repeat > 0) { names += name; repeat--; }
			return "Hello " + names;
		}
		public String convert(int a) { return String.valueOf(a); }		
		protected int increase(int a) { return a++; }
		
	}//end HelloImpl
	
	//@Anno("c") 
	static class HelloWorld {
		public void hello() { System.out.println("Hello World"); }
		public void printUser(User u) { System.out.println(u); }
	}
	
	static public class User {
		int id; String name;
		public String toString() { return "User [id=" + id + ", name=" + name + "]"; }
	}
	
	
	
}
