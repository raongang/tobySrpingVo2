package com.raon.spring3.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raon.spring3.web.AbstractDispatcherServletTest;

/**
 * @author raongang
 */
public class ValidatorTest extends AbstractDispatcherServletTest{
	
	/** validator 용도
	 *   - @ModelAttirbute binding
	 *   - 비지니스로직에서 검증로직 분리
	 *   
	 *   validator interface 구성
	 *    - supports()  : 검증할수 있는 object type check
	 *    - validate() : 검증
	 *   
	 * @throws ServletException
	 * @throws IOException
	 */
	
	//사용자 입력 폼을 처리하는 컨트롤러 메드에서 사용할 UseValidator
	//Validator는 싱글톤 빈으로 등록되어서 사용가능.
	//Controller 내에서 validator사용예제	
	@Test
	public void atrValid() throws ServletException, IOException{
		
		setClasses(UserValidator.class,UserController.class);
		initRequest("/add.do").addParameter("id", "1").addParameter("age","1");
		runService();
	}


	/**
	 * 	 @Validate를 이용한 검증
	     jsr303 -> Bean Validation @Javax.validation.Valid Annotation
		 jsr303 빈검증 -> LocalValidatorFactoryBean 을 이용해서 사용가능.
		 LocalValidatorFactoryBean -> jsr-303의 검증기능을 스프링의 Validator처럼 사용할수 있게 해주는 일종의 어댑터
		 LocalValidatorFactoryBean을 빈으로 등록시 Controller에서 Validator타입으로 DI및 @InitBinder에서 WebDataBinder에 설정하거나 코드에서 직접 Validator를 사용가능
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void jsr303() throws ServletException, IOException{
		
		/* 여기서 빈 등록. 
		 * 
		 *  <bean id="localValidatior class="org.springframework.validation.beanvalidation.LocalValidationFactoryBean" />
		 * */ 
		setClasses(UserController2.class, LocalValidatorFactoryBean.class);
		initRequest("/add.do").addParameter("id", "1").addParameter("age", "-1");
		runService();
		
	}
	
	@Controller
	static class UserController2{
		@Autowired Validator validator;
		
		@InitBinder
		public void init(WebDataBinder dataBinder) {
			dataBinder.setValidator(this.validator);
		}
		
		@RequestMapping("/add")
		public void add(@ModelAttribute @Valid User user, BindingResult result) {
			System.out.println("============== UserController2 add Start ==============");
			System.out.println(user);
			System.out.println(result);
		}
	}
	
	
	@Controller
	static class UserController {
		
		@Autowired UserValidator validator;
		
		/**
		 * @param dataBinder
		 * 	  커스텀 프로퍼티에디터를 @RequestParam같은 메소드 파라미터 바인딩에 적용할려면 WebDataBinder에 프로퍼티 에디터를 직접 등록해야 함. 
	          WebDataBinder는 내부적으로 만들어지기 때문에, 스프링이 제공하는 WebDataBinder 초기화 메소드인 @InitBinder를 사용함.
		 */
		@InitBinder
		public void init(WebDataBinder dataBinder) {
			dataBinder.setValidator(this.validator);
		}
		
		@RequestMapping("/add.do")
		public void add(@ModelAttribute User user, BindingResult result) {
			
			System.out.println("UserController Add");
			
			validator.validate(user, result);
			
			if(result.hasErrors()) {
				System.out.println("error");
			}else {
				System.out.println("no error");
			}
			System.out.println("user : " + user);
			System.out.println("result : " + result);
			System.out.println();
			
		}
	}
	
	static class UserValidator implements Validator{
		
		@Override
		public boolean supports(Class<?> clazz) {
			// TODO Auto-generated method stub
			//User class는 clazz로 assign할수 있다.
			return (User.class.isAssignableFrom(clazz));
		}

		@Override
		public void validate(Object target, Errors errors) {
			System.out.println("UserValidator validate");
			
			// TODO Auto-generated method stub
			User user = (User)target;
			
			/*
			if(user.getName()==null || user.getName().length() ==0)
				errors.rejectValue("name", "field.required");
			*/
			
//			위의 if문과 동일 기능.
//			"field.required" 는 message.properties파일의 프로퍼티키값.
			ValidationUtils.rejectIfEmpty(errors, "name", "field.required");
			ValidationUtils.rejectIfEmpty(errors, "age", "field.required");
		
//			rejectValue -> 필드이름 지정, reject() -> 글로벌에러			
			if(errors.getFieldError("age")==null)
				if(user.getAge()<0) errors.rejectValue("name","field.min", new Object[] {0}, "min default");
		}
		
	}//end UserValidator
	
	public static class User {
		int id;
		//jsr-303 빈검증 ( ※ 빈 문자열은 검증되지 않음 ) 
		@NotNull
		String name;
		@Min(0)
		Integer age;
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
		
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "User [age=" + age + ", id=" + id + ", name=" + name + "]";
		}
	}
	
	
	
	
}//end class
