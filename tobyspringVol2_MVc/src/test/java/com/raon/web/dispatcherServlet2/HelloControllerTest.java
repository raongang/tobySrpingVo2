package com.raon.web.dispatcherServlet2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.raon.web.ConfigurableDispatcherServlet;

public class HelloControllerTest {

	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	//DispatcherServlet 테스트 ( 테스트를 위해 DispathcerServlet을 전략을 통해 확장함 ) 
	@Test
	public void dispatcherTest() throws ServletException, IOException {
		ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
		
		//설정파일 위치 지정 - src/test/resources에 있을 경우에는 path를 어떻게 잡아야하나?
		// getClass() 객체가 속한 클래스의 정보.
		servlet.setRelativeLocations(getClass(), "spring-servlet.xml");
		
		//XML설정없이 직접 빈 등록.
		servlet.setClasses(HelloSpring.class);
		
		/* ServletConfig를 만들어서 서블릿 초기화 , 서블릿은 최초요청시 단한번 초기화
		   init호출시 ServletConfig를 생성해주며, 서블릿당 하나가 생성됨.
		 */
		servlet.init(new MockServletConfig("spring"));
		
		//요청을 만들어서 서블릿을 실행
		MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
		req.addParameter("name", "raon");
		MockHttpServletResponse res = new MockHttpServletResponse();
		servlet.service(req, res);
		
		//JSP View이용시 적절한 모델과 뷰를 리턴했는지 검증 
		ModelAndView mav = servlet.getModelAndView();
		
		assertThat(mav.getViewName(), is("hello"));
		assertThat((String)mav.getModel().get("message"),is("hello raon"));
		assertThat(servlet.getServletName(),is("spring"));
	}//dispatcherTest
	
	
	/* 등록된 bean list 출력 */
	@Test
	public void beanDefinitionTest() {
		System.out.println(basePath);
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(basePath + "spring-servlet.xml");
		BeanDefinitionUtils.printBeanDefinitions(context);
	}
	
	/*
	 *  DispatcherServlet 전략과 상관없이 컨트롤러 클래스의 로직을 테스트하는게 목적이라면, 
	 *  DispatcherServlet 을 거치지 않고 컨트롤러에 바로 요청을 보내서 검증하는 방법이 낫다.
	 */
	@Test
	public void controllerTest() throws Exception {
		
		// 아래는 Resouce패스 (src/test/resources)에서 찾기 때문에 현재 위치에서 찾을려면 별도로 해야함
		//ApplicationContext ac = new GenericXmlApplicationContext("spring-servlet.xml");
		ApplicationContext ac = new GenericXmlApplicationContext(basePath + "spring-servlet.xml");
		
		HelloController helloController = ac.getBean(HelloController.class);
		
		MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
		req.addParameter("name", "Spring");
		MockHttpServletResponse resp = new MockHttpServletResponse();
		
		ModelAndView mav = helloController.handleRequest(req, resp);
		assertThat(mav.getViewName(), is("hello"));
		assertThat((String)mav.getModel().get("message"),is("hello Spring"));
	}
	
	
	
	
}//end HelloControllerTest
