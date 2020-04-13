package com.raon.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;

import com.raon.web.AbstractDispatcherServletTest;

/** 
 * javax.servlet.Servlet을 구현한 서블릿 클래스도 스프링MVC의 controller로 사용될 수 있음
 *  servlet 구현 클래스를 controller로 사용하고 테스트하는 예제.
 *  servlet controller와 SimpleServletHandlerAdapter를 이용한 샘플.
 *   
*/
public class ServletControllerTest extends AbstractDispatcherServletTest{
	
	@Test
	public void helloServletController() throws UnsupportedEncodingException, ServletException, IOException {
		
		/* 
		 *  Default BeanNamedUrlHandlerMapping 사용 및 핸들러 어앱터와 컨트롤러를 빈으로 둥록한다.
		 *  Hanlder Adapter를 빈으로 등록시 DispatcherServlet은 이를 자동으로 감지하여 디폴트 핸들러 어댑터 대신 사용.
		 *   XML 
		 *     - <bean class="org.springframework.web.servlet.handler.SimpleServletHandlerAdapter" /> 
		 */
		setClasses(SimpleServletHandlerAdapter.class, HelloServlet.class);
		initRequest("/hello").addParameter("name", "raongang");
		assertThat(runService().getContentAsString(),is("test raongang"));
	}//end helloServletController

	//contoroller 선언
	@Named("/hello")
	static class HelloServlet extends HttpServlet{
		private static final long serialVersionUID = 1L;

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// TODO Auto-generated method stub
			super.doGet(req, resp);
			String name = req.getParameter("name");
			resp.getWriter().print("test " + name);
		}
	}//end HelloServlet
	
	
}//end ServletControllerTest
