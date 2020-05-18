package com.raon.spring3.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.raon.spring3.user.domain.Level;
import com.raon.spring3.web.AbstractDispatcherServletTest;

/**
 * raongang
 * 
1. WebDataBinder에 ConversionService를 등록하는 2가지 방식
	● InitBinder를 통한 수동등록      
	  - ConversionService를 빈으로 등록하고 이를 컨트롤러가 DI받아서 @InitBinder 메소드를 통해 직접 원하는 ConversionService를 설정
	  - 직접 만든 컨버트 변환 오브젝트를 추가하기 위해서는 
	    1. ConversionService 상속한 클래스 생성-> 생성자에서 addConvert() 메소드로 등록할 컨버터를 추가 -> 확장 클래스를 빈등록
*/

public class ConverterTest extends AbstractDispatcherServletTest{
	
	@Test
	public void inheritedGenericConversionService() throws ServletException,IOException{
		setClasses(SearchController.class, MyConvertsionService.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		
		assertModel("level",Level.BASIC);
	}
	
	/* ConversionService에 컨버터 추가 
	 *   1. ConversionService를 구현한 GenericConversionService 를 상속한 클래스 선언
	 *   2. 생성자에서 addConverter를 이용하여 원하는 컨버터 등록
	 *   3. 1의 확장 클래스인 MyConvertsionService를 inheritedGenericConversionService의 setClasses에 의해 빈으로 등록
	 *   
	 */
	static class MyConvertsionService extends GenericConversionService{
		{
			this.addConverter(new LevelToStringConverter());
			this.addConverter(new StringToLevelConverter());
		}
	}
	
	public static class LevelToStringConverter implements Converter<Level, String> {
		public String convert(Level level) {
			System.out.println("LevelToStringConverter");
			System.out.println(String.valueOf(level.intValue()));
			return String.valueOf(level.intValue());
		}
	}
	public static class StringToLevelConverter implements Converter<String, Level> {
		public Level convert(String text) {
			System.out.println("StringToLevelConverter : " + Level.valueOf(Integer.parseInt(text)));
			return Level.valueOf(Integer.parseInt(text));
		}
	}
	
	@Controller
	public static class SearchController{
		
		//빈으로 등록된 MyConvertsionService를 DI.
		@Autowired 
		ConversionService conversionService;
		
		//WebDataBinder는 하나의 ConversionService 타입 오브젝트만 허용.
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.setConversionService(this.conversionService);
		}
		
		@RequestMapping("/user/search.do")
		public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level",level);
		}
	}//end SearchController
	

}
