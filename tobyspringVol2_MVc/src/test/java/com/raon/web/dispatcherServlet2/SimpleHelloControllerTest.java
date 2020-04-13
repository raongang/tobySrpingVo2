package com.raon.web.dispatcherServlet2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.raon.web.AbstractDispatcherServletTest;

/*
 *  ConfigurableDispatcherServlet 와 AbstractDispatcherServletTest를 이용한 테스트.
*/

public class SimpleHelloControllerTest extends AbstractDispatcherServletTest{
	
	@Test
	public void helloController() throws ServletException,IOException{
		
		ModelAndView mav = setRelativeLocations("spring-servlet.xml")
							.setClasses(HelloSpring.class) //빈등록
							.initRequest("/hello", RequestMethod.GET).addParameter("name", "Spring")
							.runService()
							.getModelAndView();
		
		assertThat(mav.getViewName(), is("hello"));
		assertThat((String)mav.getModel().get("message"), is("hello Spring"));
	}
	
	
	@Test
	public void helloControllerWithAssertMethods() throws ServletException, IOException{
		setRelativeLocations("spring-servlet.xml")
		.setClasses(HelloSpring.class)
		.initRequest("/hello", RequestMethod.GET).addParameter("name", "Spring")
		.runService()
		.assertModel("message", "hello Spring")
		.assertViewName("hello");
	}
	
	@Test
	public void helloControllerWithServletPath() throws ServletException, IOException{
		setRelativeLocations("spring-servlet.xml")
		.setClasses(HelloSpring.class)
		.setServletPath("/app")
		.initRequest("/app/hello", RequestMethod.GET).addParameter("name", "Spring")
		.runService()
		.assertModel("message", "hello Spring")
		.assertViewName("hello");
	}
	
	
	
}//end SimpleHelloControllerTest
