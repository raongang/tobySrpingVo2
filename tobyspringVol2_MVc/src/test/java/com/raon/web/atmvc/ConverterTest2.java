package com.raon.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import com.raon.user.domain.Level;
import com.raon.web.AbstractDispatcherServletTest;
import com.raon.web.atmvc.ConverterTest.SearchController;

/**
 * 
 * @author raongang
 *       1. WebDataBinder에 ConversionService를 등록하는 2가지 방식
		● InitBinder를 통한 수동등록      
		  - ConversionService를 빈으로 등록하고 이를 컨트롤러가 DI받아서 @InitBinder 메소드를 통해 직접 원하는 ConversionService를 설정
		  - 직접 만든 컨버트 변환 오브젝트를 추가하기 위해서는 
		    2. 추가할 컨버터 클래스를 빈으로 등록 -> ConvertServiceFactoryBean을 이용해서 프로퍼티로 DI받은 컨버터들로 초기화된 GenericConversionService 를 가져오는 방법.
 */
public class ConverterTest2 extends AbstractDispatcherServletTest{
	
	@Test
	public void compositeGenericConversionService() throws ServletException, IOException{
		setRelativeLocations("conversionservice.xml");
		setClasses(SearchController.class);
		initRequest("/user/search.do").addParameter("level","1");
		runService();
		assertModel("level",Level.BASIC);
	}
	
	
}
