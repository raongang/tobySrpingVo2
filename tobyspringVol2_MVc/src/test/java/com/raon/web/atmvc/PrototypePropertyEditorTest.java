package com.raon.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raon.web.AbstractDispatcherServletTest;

/*
 *  @InitBinder나 WebBindingInitializer는 매번 new를 이용한다. 이를 해결하기 위해 싱글톤으로 할려고 했으나, prototypeEditor는 싱글톤 불가
 *  다른 빈의 di를 받기 위해서는 자기자신도 bean이 되어야 하지만 싱글톤이 되므로, prototype으로 사용한다.
 *  prototypeEditor 가 상태값을 가지기 때문에 멀티스레드 환경에서 여러 오브젝트가 공유해서 사용하면 안된다.
 * 
 * */

public class PrototypePropertyEditorTest extends AbstractDispatcherServletTest{

	/**
	 * ====================================================================================================
	 * 
	 */
	
	@Test
	public void fackeCodePropertyEdito2222222r() throws ServletException,IOException{
		setClasses(UserController.class);
		initRequest("/add.do").addParameter("id", "1").addParameter("name", "spring").addParameter("userType", "1");
		runService();
		
		/* 
		 ● void	 
		   - RequestToViewNameResolver전략을 통해 자동생성되는 뷰 이름이 사용됨.
		   - 디폴트로 등록된 RequestToViewNameResolver은 URL을 따라서 뷰이름을 만들어준다.
		 */ 
		   
		User user = (User)getModelAndView().getModel().get("user");
		assertThat(user.getUserType().getId(),is(1));
		
		try {
			user.getUserType().getName();
			fail();
		}catch(UnsupportedOperationException e) {}
		
	}
	
	
	@Controller
	static class UserController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			//FakeCodePropertyEditor - id만 들어 있는 모조 오브젝트
			dataBinder.registerCustomEditor(Code.class, new FakeCodePropertyEditor());
		}
		
		@RequestMapping("/add")
		public void add(@ModelAttribute User user) {
			System.out.println("user >> "  +user);
		}
	}
	
	static class FakeCodePropertyEditor extends PropertyEditorSupport{
		
		public void setAsText(String text) throws IllegalArgumentException{
			Code code = new FakeCode();
			
			code.setId(Integer.parseInt(text));
			setValue(code);
		}
		public String getAsText() {
			System.out.println("getAsText execute");
			return String.valueOf(((Code)getValue()).getId());
		}
	}
	
	static class User {
		int id; String name; Code userType;
		
		public int getId() {	return id;	}
		public void setId(int id) { this.id = id; }
		
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		
		public Code getUserType() { return userType; }
		public void setUserType(Code userType) { this.userType = userType; }
		
		@Override
		public String toString() { return "User [id=" + id + ", name=" + name + ", userType=" + userType + "]";
		}		
	}//end User 
	
	
	static class FakeCode extends Code{
		public String getName() {
			throw new UnsupportedOperationException();
		}
		public void setName(String name) {
			throw new UnsupportedOperationException();
		}
	}
	
	static class Code {
		int id;
		String name;
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public String toString() {
			return "Code [id=" + id + ", name=" + name + "]";
		}
	}//end Code
	
	/**
	 * ====================================================================================================
	 *  프로토 타입 스코프 - 컨테이너에게 빈을 요청할 때마다 매번 새로운 오브젝트를 생성.
	 */
	
	// 프로토타입 도메인 오브젝트 프로퍼티 에디터 
	//  - db에서 읽어온 완전한 Code오브젝트로 변환
	//  - 다른 계층에서 사용시 제한을 받지 않음.
	//  - 다른 bean을 DI받는 오브젝트는 자신도 빈이어야 한다.
	
	@Test
	public void prototypePropertyEditor() throws ServletException, IOException{
		setClasses(UserController2.class,CodePropertyEditor.class,CodeService.class);
		
		initRequest("/add.do").addParameter("id", "1").addParameter("name", "Spring").addParameter("userType", "2");
		runService();
		User user = (User)getModelAndView().getModel().get("user");
		user.getUserType().getName();
	}
	
	@Controller
	static class UserController2{
		@Inject Provider<CodePropertyEditor> codeEditorProvider;
		
		@InitBinder 
		public void initBinder(WebDataBinder dataBinder) {
			//get() - Provider를 이용해 프로토타입 빈의 새 오브젝트를 가져온다. @Scope
			dataBinder.registerCustomEditor(Code.class,codeEditorProvider.get());
		}
		
		/**
		 * 리턴타입
		 * 	 	1) @ModelAttribute 모델 오브젝트 또는 커맨드 오브젝트            
	    		- 자동으로 컨트롤러가 리턴하는 모델에 추가됨.
	    		- 기본적으로 모델 오브젝트 이름은 파라미터 타입이름.
		 */
		@RequestMapping("/add")
		public void add(@ModelAttribute User user) {
			System.out.println(user);
		}
	}
	
	//CodePropertyEditor는 bean으로 등록될것이기 때문에 @Autowired로 다른 bean 참조 가능
	//@Component
	@Scope("prototype")
	static class CodePropertyEditor extends PropertyEditorSupport{
		@Autowired CodeService codeService; 
		
		public void setAsText(String text) throws IllegalArgumentException{
			setValue(this.codeService.getCode(Integer.parseInt(text)));
		}
		
		public String getAsText() {
			return String.valueOf(((Code)getValue()).getId()); 
		}
	}
	
	static class CodeService {
		public Code getCode(int id) {
			Code c = new Code();
			c.setId(id);
			c.setName("name");
			return c;
		}
	}
}
