package com.raon.spring31.web;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.raon.spring3.web.AbstractDispatcherServletTest;
import com.raon.spring3.web.ConfigurableDispatcherServlet;


public abstract class AbstractDispatcherServlet31Test extends AbstractDispatcherServletTest {

	@Override
	protected ConfigurableDispatcherServlet createDispatcherServlet() {
		return new ConfigurableDispatcherServlet() {
			@Override
			protected WebApplicationContext createWebApplicationContext(
					ApplicationContext parent) {
				
				AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
				wac.register(this.classes);
		        wac.setServletContext(getServletContext()); 
		        wac.setServletConfig(getServletConfig()); 
		        wac.refresh(); 
		        
		        return wac; 
			}
		};
	}

	@Override
	public AnnotationConfigWebApplicationContext getContext() {
		return (AnnotationConfigWebApplicationContext) super.getContext();
	}
}

