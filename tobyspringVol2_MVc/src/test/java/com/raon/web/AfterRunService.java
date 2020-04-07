package com.raon.web;

import java.io.UnsupportedEncodingException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/* 
 *  ConfigurableDispatcherServlet 가 준비할게 많고 테스트 코드도 들어서 DispatcherServlet를 간단히 만들수 있는
 *  테스트 지원 클래스 작성 
 */
public interface AfterRunService {
	
	String getContentAsString() throws UnsupportedEncodingException;
	
	WebApplicationContext getContext();
	
	<T> T getBean(Class<T> beanType);
	
	ModelAndView getModelAndView();
	
	AfterRunService assertViewName(String vinewname);
	
	AfterRunService assertModel(String name, Object value);
}
