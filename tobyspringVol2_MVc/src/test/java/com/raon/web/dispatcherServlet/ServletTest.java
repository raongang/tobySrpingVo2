package com.raon.web.dispatcherServlet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

/* Servlet 단위테스트 
 * 
 *   spring4.0이상부터는 MockHttpServletResponse는 servlet3.0이상 사용
 *   javax.servlet-api, spring-test maven 추가
 *   
*/
public class ServletTest {
	@Test
	public void getMehtodServlet() throws ServletException, IOException{
	
		MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
		req.addParameter("name", "mockParameter");
		
		/* 만일 세션을 이용한다면 ? 
		MockHttpSession session = new MockHttpSession();
		session.putValue("cart", new Hello());
		req.setSession(session);
		*/
		MockHttpServletResponse res = new MockHttpServletResponse();

		SimpleGetServlet servlet = new SimpleGetServlet();
		servlet.service(req, res);
		servlet.init();
		
		assertThat(res.getContentAsString(),is("<HTML><BODY>Hello mockParameter</BODY></HTML>"));
		assertThat(res.getContentAsString().indexOf("Hello mockParameter") >0, is(true));
		assertThat(res.getContentAsString().contains("Hello mockParameter"), is(true));
	}
}
