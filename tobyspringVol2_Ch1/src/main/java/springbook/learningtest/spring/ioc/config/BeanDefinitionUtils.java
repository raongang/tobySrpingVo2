package springbook.learningtest.spring.ioc.config;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/** 전용 태그에 의해 등록되는 빈 및 빈의 속성 설정을 확인하는 소스 */
public class BeanDefinitionUtils {

	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	//GenericApplicationContext - 가장 일반적 ApplicationContext 구현클래스
	public static void printBeanDefinitions(GenericApplicationContext gac) {
		 List<List<String>> roleBeanInfos  = new ArrayList<List<String>>();
		 
		 roleBeanInfos.add(new ArrayList<String>());
		 roleBeanInfos.add(new ArrayList<String>());
		 roleBeanInfos.add(new ArrayList<String>());
		 
		 for(String name: gac.getBeanDefinitionNames()) {

			 /*
			  *  BeanDefinition 인터페이스에 정의되어 있는 ROLE_로 시작하는 상수.
			  *
			  *   ROLE_APPLICATION(0) - 애플리케이션 로직 빈과 애플리케이션 인프라 빈처럼 애플리케이션이 동작중에 사용되는 빈. 애플리케이션을 구성하는 빈.
			  *   ROLE_SUIPPORT(1) - 복합구조의 빈을 정의할때 보조적으로 사용되는 빈의 역할을 지정하려고 정의.(실제로는 거의 사용되지 않음으로 무시)
			  *   ROLE_INFRASTRUCTURE(2) - <context:annotation-config>같은 전용 태그에 의해 등록되는 컨테이너 인프라 빈들
			 */
			 int role = gac.getBeanDefinition(name).getRole();
			 List<String> beanInfos = roleBeanInfos.get(role);
			 
			 beanInfos.add(role+"\t"+name+"\t"+gac.getBean(name).getClass().getName());
		 }
		 
		 for(List<String> beanInfos : roleBeanInfos) {
			 for(String beanInfo : beanInfos) {
				 System.out.println(beanInfo);
			 }
		 }
	}//end printBeanDefinitions
	
	@Test
	public void beanDefinitionTest() {
		System.out.println("basePath : " + basePath);
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(basePath + "simpleConfig.xml");
		printBeanDefinitions(context);
		//SimpleConfig sc = context.getBean(SimpleConfig.class);
		//sc.hello.sayHello();
	}
	
	
}
