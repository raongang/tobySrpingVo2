package com.raon.web.exception;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.raon.web.AbstractDispatcherServletTest;

/*
 * HandlerExceptionResovler는  HandlerExceptionResovler Interface를 구현해서 사용한다.
 * */
public class HandlerExceptionResovler extends AbstractDispatcherServletTest {
	
	/** AnnotationMethodHandlerExceptionResolver 예제 */
	@Test
	public void AnnotationMethodHandlerExceptionResolverMehtod()  throws ServletException, IOException{
		setClasses(HelloCon.class);
		runService("/hello");
		assertViewName("dataexception");
		System.out.println(getModelAndView().getModel().get("msg"));	
	}
	
	@RequestMapping
	static class HelloCon{
		@RequestMapping("/hello")
		public void hello() {
			/* maven spring-dao required
			 DataRetrievalFailureException
			  - 기본키(primary key)로 레코드를 찾지 못하는 경우 등과 같이 어떤 데이터를 가져오지 못하는 경우
			*/
			if(1==1) throw new DataRetrievalFailureException("exception");
		}
	}//end HelloCon
	
	/*
	 *   DataAccessException은  일괄된 예외처리를 하기 위함.
	 *   SQLException이나 HibernateException 등과 같은 특정 기술에 의존적인 예외를 던지지 않는다.
		 모든 DAO Exceptiond는 org.springframework.dao.DataAccessException의 서브 클래스
		 RuntimeException이기 때문에 비검사 예외(unchecked exception)에 속한다.
	 */
	@ExceptionHandler(DataAccessException.class)
	public ModelAndView dataAccessExceptionHandler(DataAccessException ex){
		return new ModelAndView("dataexception").addObject("msg",ex.getMessage());
	}
	
	
	/** ResponseStatusExceptionResolver 예제 */
	@Test
	public void responseStatus() throws ServletException, IOException{
		setClasses(HelloCon2.class);
		runService("/hello");
		System.out.println("response.getStatus() : " + response.getStatus());
		System.out.println(response.getErrorMessage());
	}
	
	@RequestMapping
	static class HelloCon2{
		@RequestMapping("/hello")
		public void hello() {
			if(1==1) throw new NotInServiceException();
		}
	}//end HelloCon2
	
	@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="서비스 긴급 점검중")
	static class NotInServiceException extends RuntimeException {
	}
	
}
