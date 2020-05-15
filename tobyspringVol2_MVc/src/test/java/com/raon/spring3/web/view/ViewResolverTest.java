package com.raon.spring3.web.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.raon.spring3.web.AbstractDispatcherServletTest;

public class ViewResolverTest extends AbstractDispatcherServletTest{
	
	@Test
	public void jstlView() throws ServletException, IOException{
		setRelativeLocations("jstlview.xml");
		setRelativeLocations("jstlview.xml")
		.setClasses(HelloController.class) //bean등록
		.runService("/hello"); //servlet 실행이후에 response를 리턴한다.
		System.out.println(this.response.getForwardedUrl());
	}
	
	public void print() {
		System.out.println("ViewResolverTest Print");
	}

	// DefaultAnnotationHandlerMapping 사용.
	//handleRequest - Controller Type Hanlder를 담당하는 SimpleControllerHandlingAdapter를 통해 DispatcherServlet으로부터 호출된것.
	@RequestMapping("/hello")
	public static class HelloController implements Controller{

		/* 
		 *  비교
		 *	String message = this.helloSpring.sayHello(name);
		 *	Map<String,Object> model = new HashMap<String,Object>();
		 *	model.put("message", message);
		 *	return new ModelAndView("hello",model);
		*/
		
		@Override
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// TODO Auto-generated method stub
			return new ModelAndView("hello").addObject("message", "Hello Spring");
		}
	}
}
