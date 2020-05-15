package com.raon.spring3.web.atmvc;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;


import java.text.ParseException;
import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.raon.spring3.user.domain.Level;
import com.raon.spring3.web.AbstractDispatcherServletTest;


/**
 * @author raongang
 *  - 커스컴 fomatter를 추가할 필요가 없을 경우 
 *  <bean id="conversionService class="org.springframework.format.support.FormattingConversionServiceFactoryBean" />
 *  이걸 controller에서 DI받아서 @InitBinder를 통해 지정해도 되고, 
 *  
 *  
 *  - 전역 fomatter 설정
 *  ConfigurableWebBindingInitializer 이용해 전체 일괄 적용가능.
 *   
 * *
 */
public class FomatterBidingTest extends AbstractDispatcherServletTest{
		
		@Test
		public void webBindingInit() throws ServletException, IOException {
			setClasses(Config.class, UserController.class);
			initRequest("/add.do", "POST");
			addParameter("id", "1").addParameter("name", "Spring").addParameter("date", "02/03/01");
			addParameter("level", "3");
			runService();
		}
		@Controller static class UserController {
			@RequestMapping("/add") public void add(@ModelAttribute User user) {
				System.out.println(user);
			}
		}
		@Configuration static class Config {
			@Autowired FormattingConversionService conversionService;
			
			@Bean public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter() {
				return new AnnotationMethodHandlerAdapter() {{
					setWebBindingInitializer(webBindingInit());
				}};
			}
			
			@Bean public WebBindingInitializer webBindingInit() {
				return new ConfigurableWebBindingInitializer() {{
					setConversionService(Config.this.conversionService);
				}};
			}
			
			@Bean public FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean() {
				return new FormattingConversionServiceFactoryBean() {{
//					setConverters(new LinkedHashSet(Arrays.asList(new Converter[] {new LabelToStringConverter(), new StringToLabelConverter()}))); // convert 적용
					}
					protected void installFormatters(FormatterRegistry registry) {
						installFormatters(registry);
						registry.addFormatterForFieldType(Level.class, new LabelStringFormatter());
					}
				};
			}
			
			// formatter
			static class LabelStringFormatter implements Formatter<Level> {
				public String print(Level level, Locale locale) {
					return String.valueOf(level.intValue());
				}

				public Level parse(String text, Locale locale) throws ParseException {
					return Level.valueOf(Integer.parseInt(text));
				}
			}
			
		}
		
		static class User {
			int id;
			String name;
			Level level;
			@DateTimeFormat(pattern="dd/yy/MM")
			Date date;
			
			public Date getDate() {
				return date;
			}
			public void setDate(Date date) {
				this.date = date;
			}
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Level getLevel() {
				return level;
			}
			public void setLevel(Level level) {
				this.level = level;
			}
			@Override
			public String toString() {
				return "User [date=" + date + ", id=" + id + ", level=" + level + ", name=" + name + "]";
			}
		}
	}
