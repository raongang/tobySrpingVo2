package com.raon.web.atmvc;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raon.user.domain.Level;
import com.raon.web.AbstractDispatcherServletTest;


/**
 * @author raongang
 *  - ConfigurableWebBindingInitializer를 이용한 ConversionService 등록
 *
 */
public class FomatterBidingTest extends AbstractDispatcherServletTest{

	@Test
	public void WebBidingInit() throws ServletException, IOException{
		setClasses(Config.class, UserController.class);
		initRequest("/add.do", "POST");
		addParameter("id","1").addParameter("name", "Spring").addParameter("data", "02/03/01");
		addParameter("level","3");
		runService();
	}
	
	@Controller
	static class UserController{
		@RequestMapping("/add")
		public void add(@ModelAttribute User user) {
			System.out.println("user : " + user);
		}
	}
	
	
	@Configuration 
	static class Config{
		
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
