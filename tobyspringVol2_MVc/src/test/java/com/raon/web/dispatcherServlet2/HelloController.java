package com.raon.web.dispatcherServlet2;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/* Servlet Context Test  */

public class HelloController implements Controller{

	//부모 컨텍스트인 root-context로부터 DI받음
	@Autowired HelloSpring helloSpring;
	
	//handleRequest - Controller Type Hanlder를 담당하는 SimpleControllerHandlingAdapter를 통해 DispatcherServlet으로부터 호출된것.
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("test");
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		System.out.println("name : " + name);
		
		String message = this.helloSpring.sayHello(name);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("message", message);
		
		//return new ModelAndView("/WEB-INF/view/hello",model);
		return new ModelAndView("hello",model);
	}

}
