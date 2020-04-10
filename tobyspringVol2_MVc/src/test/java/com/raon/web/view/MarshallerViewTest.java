package com.raon.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.raon.web.AbstractDispatcherServletTest;

public class MarshallerViewTest extends AbstractDispatcherServletTest{
	
	@Test
	public void marshallingView() throws ServletException, IOException{
		setRelativeLocations("marshallingview.xml");
		initRequest("/hello").addParameter("name", "Spring");
		runService();
		System.out.println("-----------------------------------");
		System.out.println(getContentAsString());
		System.out.println("-----------------------------------");
		assertThat(getContentAsString().indexOf("<info><message>Hello Spring</message></info>") >= 0, is(true));
	}

	@RequestMapping("/hello")
	public static class HelloController implements Controller{
		//같은 type의 뷰가 여러개 존재할수 있으니, 빈 이름으로 매핑
		@Resource MarshallingView helloMarshallingView;
		
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("info", new Info("Hello "+ request.getParameter("name")));
			return new ModelAndView(helloMarshallingView,model);
		}
	}
	
	
}
