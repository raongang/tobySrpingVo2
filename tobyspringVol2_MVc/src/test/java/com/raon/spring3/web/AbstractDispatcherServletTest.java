package com.raon.spring3.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;

import org.junit.After;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/* 
 *  ConfigurableDispatcherServlet 가 준비할게 많고 테스트 코드도 들어서 DispatcherServlet를 간단히 만들수 있는
 *  테스트 지원 클래스 작성 
 */

public abstract class AbstractDispatcherServletTest implements AfterRunService{

	//서블릿 목오브젝트는 서브클래스에서 직접 접근가능.
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	protected MockServletConfig config = new MockServletConfig("spring");
	protected MockHttpSession session;
	
	private ConfigurableDispatcherServlet dispatcherServlet;
	
	//서블릿 컨텍스트 생성을 위한 메타정보는 클래스, 클래스패스, 상대클래스패스 끝 
	private Class<?>[] classes;
	private String[] locations;
	private String[] relativeLocations;
	private String servletPath;
	
	public AbstractDispatcherServletTest setLocations(String ...locations) {
		this.locations = locations;
		return this;
	}
	
	public void print() {
		System.out.println("AbstractDispatcherTest Print");
	}
	
	//자식의 this로 들어와서 부모의 this를 반환
	public AbstractDispatcherServletTest setRelativeLocations(String ...relativeLocations) {
		System.out.println("==============================");
		System.out.println(this); 
		System.out.println("==============================");
		this.relativeLocations = relativeLocations;
		
		return this;
	}
	
	public AbstractDispatcherServletTest setClasses(Class<?> ...classes) {
		this.classes = classes;
		return this;
	}
	
	public AbstractDispatcherServletTest setServletPath(String servletPath) {
		if (this.request == null)
			this.servletPath = servletPath;
		else
			this.request.setServletPath(servletPath);
		return this;
	}
	
	public AbstractDispatcherServletTest initRequest(String requestUri, String method) {
		this.request = new MockHttpServletRequest(method, requestUri);
		this.response = new MockHttpServletResponse();
		if (this.servletPath != null) this.setServletPath(this.servletPath);
		return this;
	}
	
	public AbstractDispatcherServletTest initRequest(String requestUri, RequestMethod method) {
		return this.initRequest(requestUri, method.toString());
	}
	
	public AbstractDispatcherServletTest initRequest(String requestUri) {
		initRequest(requestUri, RequestMethod.GET);
		return this;
	}
	
	public AbstractDispatcherServletTest addParameter(String name, String value) {
		if (this.request == null) 
			throw new IllegalStateException("request가 초기화되지 않았습니다.");
		this.request.addParameter(name, value);
		return this;
	}
	
	public AbstractDispatcherServletTest buildDispatcherServlet() throws ServletException {
		
		if (this.classes == null && this.locations == null && this.relativeLocations == null) { 
			throw new IllegalStateException("classes와 locations 중 하나는 설정해야 합니다");
		}
		
		this.dispatcherServlet = createDispatcherServlet();
		this.dispatcherServlet.setClasses(this.classes);
		this.dispatcherServlet.setLocations(this.locations);
		if (this.relativeLocations != null)
			this.dispatcherServlet.setRelativeLocations(getClass(), this.relativeLocations);
		
		this.dispatcherServlet.init(this.config);
		
		return this;
	}
	
	protected ConfigurableDispatcherServlet createDispatcherServlet() {
		return new ConfigurableDispatcherServlet();
	}

	public AfterRunService runService() throws ServletException, IOException {
		if (this.dispatcherServlet == null) buildDispatcherServlet(); 
		if (this.request == null) 
			throw new IllegalStateException("request가 준비되지 않았습니다");
		this.dispatcherServlet.service(this.request, this.response);
		
		return this;
	}
	
	public AfterRunService runService(String requestUri) throws ServletException, IOException {
		initRequest(requestUri);
		runService();
		return this;
	}
	
	public AfterRunService runService(String requestUri, String method) throws ServletException, IOException {
		initRequest(requestUri, method);
		runService();
		return this;
	}
	
	@Override
	public ConfigurableWebApplicationContext getContext() {
		if (this.dispatcherServlet == null) 
			throw new IllegalStateException("DispatcherServlet이 준비되지 않았습니다");
		return (ConfigurableWebApplicationContext)this.dispatcherServlet.getWebApplicationContext();
	}
	
	@Override
	public <T> T getBean(Class<T> beanType) {
		if (this.dispatcherServlet == null) 
			throw new IllegalStateException("DispatcherServlet이 준비되지 않았습니다");
		return this.getContext().getBean(beanType);
	}
	
	
	@Override
	public ModelAndView getModelAndView() {
		return this.dispatcherServlet.getModelAndView();
	}

	@Override
	public AfterRunService assertModel(String name, Object value) {
		System.out.println("this.getModelAndView().getModel().get(name) : " + this.getModelAndView().getModel().get(name));
		assertThat(this.getModelAndView().getModel().get(name), is(value));
		return this;
	}

	@Override
	public AfterRunService assertViewName(String viewName) {
		System.out.println(this.getModelAndView().getViewName());
		assertThat(this.getModelAndView().getViewName(), is(viewName));
		return this;
	}

	@Override
	public String getContentAsString() throws UnsupportedEncodingException {
		return this.response.getContentAsString();
	}
	
	@After
	public void closeServletContext() {
		if (this.dispatcherServlet != null) {
			((ConfigurableApplicationContext)dispatcherServlet.getWebApplicationContext()).close();
		}
	}
}

