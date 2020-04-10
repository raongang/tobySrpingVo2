package com.raon.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.raon.web.AbstractDispatcherServletTest;

public class RedirectViewTest extends AbstractDispatcherServletTest{
	 
	/*
	 - HttpServletResponse의 sendRedirect()를 호출해주는 기능을 가진 view
	 - 실제 View가 생성되는게 아니라 url만 만들어져 다른 페이지로 리다이렉트 된다.
	*/
	
	@Test
	public void redirectView() throws ServletException, IOException{
		setClasses(HelloController.class);
		runService("/hello");
		assertThat(this.response.getRedirectedUrl(),is("/main?name=Spring"));
	}
	
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
			// 상대경로를 이용하고 싶을 경우 true옵션 추가
			return new ModelAndView(new RedirectView("/main",true)).addObject("name","Spring");
		}
		
	}
	

}
