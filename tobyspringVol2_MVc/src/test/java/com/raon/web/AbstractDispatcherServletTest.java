package com.raon.web;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;

/* 
 *  ConfigurableDispatcherServlet 가 준비할게 많고 테스트 코드도 들어서 DispatcherServlet를 간단히 만들수 있는
 *  테스트 지원 클래스 작성 
 */

public abstract class AbstractDispatcherServletTest implements AfterRunService{

	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	protected MockServletConfig config = new MockServletConfig("spring");
	protected MockHttpSession session;
	
}
