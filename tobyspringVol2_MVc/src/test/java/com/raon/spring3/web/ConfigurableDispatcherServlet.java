package com.raon.spring3.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

/* 테스트를 위한 dispatcherServlet 확장 
 *   - WEB-INF밑에 매번 테스트용 설정 파일을 두고 이를 web.xml에 설정해 가면서 테스트하기에는 불편.
 *   - context 초기화 기능과 편리한 설정기능을 추가한 테스트용 dispatcherServlet 확장 클래스.
 * */
public class ConfigurableDispatcherServlet extends DispatcherServlet{

	private static final long serialVersionUID = 1L;
	
	//설정방법을 확장해서 클래스와 XML, 파일 두가지 방법을 모두 지원
	protected Class<?>[] classes;
	private String[] locations;
	
	//컨트롤러가 DispatcherServlet에 돌려주는 모델과 뷰 정보를 저장해서 테스트에서 확인할 수 있게 해준다.
	private ModelAndView modelAndView;
	
	public ConfigurableDispatcherServlet() {}
	
	public ConfigurableDispatcherServlet(String[] locations) {
		this.locations = locations;
	}
	
	public ConfigurableDispatcherServlet(Class<?> ...classes) {
		this.classes = classes;
	}
	
    public void setLocations(String ...locations) {
		this.locations = locations;
	}
	
    public void setClasses(Class<?> ...classes) {
		this.classes = classes;
	}
    
	//주어진 클래스로부터 상대적인 위치의 클래스에 있는 설정파일을 지정할 수 있게 해준다.
    public void setRelativeLocations(Class clazz, String ...relativeLocations) {
    	String[] locations = new String[relativeLocations.length];
    	String currentPath = ClassUtils.classPackageAsResourcePath(clazz) + "/";
    	
    	for(int i=0; i<relativeLocations.length; i++) {
    		System.out.println("currentPath : " + currentPath + " \t relativeLocations[" + i + "] : " + relativeLocations[i]);
    		locations[i] = currentPath + relativeLocations[i]; 
    	}
    	this.setLocations(locations);
    }
	
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		modelAndView = null;
		super.service(req, res); // 여기서 HelloController handleRequest호출됨. 
	}
	
	//DispatcherServlet 의 서블릿 컨텍스트를 생성하는 메소드를 오버라이딩해서 테스트용 메타정보를 이용해서 서블릿 컨텍스트를 생성하게 함.
	@Override
	protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
		AbstractRefreshableWebApplicationContext wac = new AbstractRefreshableWebApplicationContext() {
			protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
            	if (locations != null) {
	            	XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beanFactory);
	            	xmlReader.loadBeanDefinitions(locations);
            	}
            	if (classes != null) {
	                AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory); 
	                reader.register(classes); 
            	}
			}
		};
			
        wac.setServletContext(getServletContext()); 
        wac.setServletConfig(getServletConfig()); 
        wac.refresh(); 
        
        return wac; 
		
	}//end createWebApplicationContext
	
	/*
	 *   뷰를 실행하는 과정을 가로채서 컨트롤러갸 돌려준 ModelAndView 정보를 따로 저장해준다. 테스트에서 HttpServletResponse를 확인하는 대신 
	 *   컨트롤러가 리턴한 ModelAndView를 검증할 수 있게 해준다.
	 * */
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.modelAndView = mv;
		super.render(mv, request, response);
	}
	
	public ModelAndView getModelAndView() {
		return modelAndView;
	}
	
}
