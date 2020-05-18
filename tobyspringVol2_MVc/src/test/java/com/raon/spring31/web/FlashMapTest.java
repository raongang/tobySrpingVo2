package com.raon.spring31.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.raon.spring3.web.AbstractDispatcherServletTest;

public class FlashMapTest extends AbstractDispatcherServletTest{
	
	@Test
	public void flsashMapTest() throws ServletException, IOException{
		
		setClasses(PostController.class, RedirectController.class, OtherController.class);
		runService("/flash");
		
		assertThat(this.getModelAndView().getViewName(), is("redirect:/redirect"));
		HttpSession sessionSaved = request.getSession();
		
		//1st request
		initRequest("/redirect");
		request.setSession(sessionSaved);
		runService();
		
		sessionSaved = request.getSession();
		
		//2st request
		initRequest("/redirect");
		request.setSession(sessionSaved);
		
		try {
			runService();
			fail();
		}catch(Exception e) {
			System.out.println(e.getCause()); // NullPointerException 발생.
			//Junit4
			//assertThat(e.getCause(), is(NullPointerException.class));
		}
		
	}//end flashMapTest
	
	@Test
	public void flashMapWithRequestPath() throws ServletException,IOException{
		setClasses(PostController.class, RedirectController.class, OtherController.class);
		
		runService("/flash");
		
		HttpSession sessionSaved = request.getSession();
		
		//other
		initRequest("/other");
		request.setSession(sessionSaved);
		
		try {
			runService();
			fail();
		}catch(Exception e) {
			System.out.println(e.getCause());
		}
		
		sessionSaved = request.getSession();
		
		//redirect
		initRequest("/redirect");
		request.setSession(sessionSaved);
		runService();
		
	}
	
	@RequestMapping("/flash")
	static class PostController implements Controller{
		@Override
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
			//DispatcherServlet이 미리 준비한 FlashMap GET
			FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
			fm.put("message","Saved");
			fm.setTargetRequestPath("/redirect");
			return new ModelAndView("redirect:/redirect");
		}
	}
	
	@RequestMapping("/redirect")
	static class RedirectController implements Controller{
		@Override
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			assertThat((String)RequestContextUtils.getInputFlashMap(request).get("message"),is("Saved"));
			return new ModelAndView("next");
		}
	}
	
	@RequestMapping("/other")
	static class OtherController implements Controller{
		@Override
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
			assertThat((String)RequestContextUtils.getInputFlashMap(request).get("message"), is("Saved"));
			return new ModelAndView("next");
		}
	}
	
	
}
