package com.raon.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.raon.web.AbstractDispatcherServletTest;

public class SessionAttributesTest extends AbstractDispatcherServletTest{

	@Test
	public void sessionAttr() throws ServletException, IOException {
		setClasses(UserController.class);
		initRequest("/user/edit").addParameter("id", "1");
		runService();
	
		HttpSession session = request.getSession(); 
		assertThat(session.getAttribute("user"), is(getModelAndView().getModel().get("user")));

		initRequest("/user/edit","POST").addParameter("id", "1").addParameter("name", "Spring2");
		request.setSession(session);
		runService();
		
		assertThat(((User)getModelAndView().getModel().get("user")).getEmail(),is("mail@spring.com"));
		assertThat(session.getAttribute("user"),is(nullValue()));
		
	}
	
	/** Controller */
	@Controller
	//Model 정보중에 user가 있으면 이를 session에 저장한다.
	@SessionAttributes("user")
	static class UserController{
		
		//스프링은  리턴 타입이 미리 지정된 타입이나 void가 아닌 단순 오브젝트라면 이를 모델 오브젝트로 인식해서 모델에 자동으로 추가해준다.
		//User 오브젝트가 Model에 추가됨.
		@RequestMapping(value="/user/edit", method=RequestMethod.GET)
		public User form(@RequestParam int id) {
			System.out.println("form Start");
			return new User(1,"Spring", "mail@spring.com");
		}
		

		 //스프링은  리턴 타입이 미리 지정된 타입이나 void가 아닌 단순 오브젝트라면 이를 모델 오브젝트로 인식해서 모델에 자동으로 추가해준다.
		//@ModelAttribute 로 지정된 파라미터가 세션에 있을 경우 이를 가져오고, @ModelAttribute는 폼에서 전달된 필드정보를 모델 오브젝트의 프로퍼티로 넣어준다.
		//즉 form에서 등록된 User를 가져오고 POST를 통해서 들어오는 데이터를 User의 프로퍼티로 넣는다. ( GET에서의 User정보 + post에서 수정한 정보의 프로퍼티 정보 ) 
		@RequestMapping(value="/user/edit", method=RequestMethod.POST)
		public void submit(@ModelAttribute User user, SessionStatus sessionStatus) {
			System.out.println("submit Start");
			sessionStatus.setComplete();
		}
	}//end UserController
	
	
	/** dto */
	static class User {
		int id;
		String name;
		String email;

		public User(int id, String name, String email) {
			this.id = id;
			this.name = name;
			this.email = email;
		}

		public User() {
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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String toString() {
			return "User [email=" + email + ", id=" + id + ", name=" + name + "]";
		}
	}//end User
	
}//end SessionAttributesTest

