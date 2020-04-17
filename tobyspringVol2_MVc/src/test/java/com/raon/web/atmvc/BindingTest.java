package com.raon.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.raon.user.domain.Level;
import com.raon.web.AbstractDispatcherServletTest;

public class BindingTest extends AbstractDispatcherServletTest {

	@Test
	public void defaultPropertyEditor() throws ServletException, IOException{
		setClasses(DefaultPEController.class);
		initRequest("/hello.do").addParameter("charset", "UTF-8");
		runService();
		assertModel("charset",  Charset.forName("UTF-8"));
	}
	
	@Controller
	static class DefaultPEController{
		@RequestMapping("/hello")
		public void hello(@RequestParam Charset charset, Model model) {
			model.addAttribute("charset", charset);
		}
	}//end DefaultPEController
	
	/**
	 * ====================================================================================================
	 */
	/* 커스컴 프로퍼티 에디터 */
	@Test
	public void levelPropertyEditor() {
		LevelPropertyEditor levelEditor = new LevelPropertyEditor();
		
		levelEditor.setValue(Level.BASIC);
		assertThat(levelEditor.getAsText(),is("1"));
		
		levelEditor.setAsText("3");
		assertThat((Level)levelEditor.getValue(), is(Level.GOLD));
	}
	
	/* @InitBinder 테스트 */
	@Test
	public void levelTypeParameter() throws ServletException, IOException{
		
		setClasses(SearchController.class);
		//addPrameter -> name,value
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level",Level.BASIC);
		
	}
	
	@Controller
	static class SearchController{
		/* WebDataBinder 초기화, initBinder 메소드는  메소드 파라미터를 바인딩하기 전에 자동 호출.
		   HTTP요청을 바인딩하는 모든 작업에 적용됨
		   WebDataBinder 바인딩 적용 대상 - @RequestParam parameter, @CookieValue parameter, @RequestHeader parameter, @PathVariable Parameter, @ModelAttribute parameter
		*/
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			//param : 적용타입, 프로퍼티 에디터
			dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		}
		
		@RequestMapping("/user/search")
		public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level",level);
		}
	}
	
	//오브젝트 내부에 한번은 값이 저장되므로 상태를 가지게 되고, singleton bean 등록 불가능.
	static class LevelPropertyEditor extends PropertyEditorSupport {
		//변환된 값 가져오는 식.
		public String getAsText() {
			return String.valueOf(((Level)this.getValue()).intValue());
		}

		public void setAsText(String text) throws IllegalArgumentException {
			//object에 넣음.
			this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
		}
	}
	
	
	/**
	 * ====================================================================================================
	 */
	
	@Test
	public void namedPropertyEditor() throws ServletException, IOException{
		setClasses(MemberController.class);
		initRequest("/add.do").addParameter("id", "10000").addParameter("age", "10000");
		runService();
		System.out.println(getModelAndView().getModel().get("member"));
		
	}
	
	@Controller
	static class MemberController{
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(int.class, "age", new MinMaxPropertyEditor(0,200));
		}
		
		//@ModelAttribute 자동으로 컨트롤러가 리턴하는 모델에 추가됨.
		@RequestMapping("/add")
		public void add(@ModelAttribute Member member) { System.out.println("/add call!!");}
	}
	
	/* WebDataBinder에 특정 이름의 프로퍼티에만 적용할 커스컴 프로퍼티 에디터
	   이름이 필요하므로 @RequestParam같은 단일 파라미터 바인딩이 아닌 @ModelAttribute로 지정된 모델 오브젝트의 프로퍼티 바인딩에 사용.
	*/ 
	static class MinMaxPropertyEditor extends PropertyEditorSupport{
		int min;
		int max;
		
		public MinMaxPropertyEditor(int min, int max) {
			super();
			this.min = min;
			this.max = max;
		}

		public MinMaxPropertyEditor() {}

		@Override
		public String getAsText() {
			// TODO Auto-generated method stub
			System.out.println("[getAsText] enter");
			return String.valueOf((Integer)this.getValue());
			
			//getValue();
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			System.out.println("[setAsText] text : " + text);
			
			// TODO Auto-generated method stub
			Integer val = Integer.parseInt(text);
			if(val<min) val=min;
			else if(val>max) val=max;
			
			setValue(val);
		}//end 
	}//end MinMaxPropertyEditor
	
	static class Member {
		int id;
		int age;
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public int getAge() { return age; }
		public void setAge(int age) { this.age = age; }
		public String toString() { return "Member [age=" + age + ", id=" + id + "]"; }
	}
	
	/**
	 * ====================================================================================================
	 */
	
	//WebBindingInitializer 예제
	@Test
	public void webBindingInitializer() throws ServletException, IOException {
		setClasses(SearchController2.class, ConfigForWebBinidngInitializer.class);
		initRequest("/user/search").addParameter("level", "2");
		runService();
		assertModel("level", Level.SILVER);
	}
	@Controller static class SearchController2 {
		@RequestMapping("/user/search") public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	
	@Configuration static class ConfigForWebBinidngInitializer {
		@SuppressWarnings("deprecation")
		@Bean public AnnotationMethodHandlerAdapter annotationMethodHandlerAdaptor() {
			return new AnnotationMethodHandlerAdapter() {{
				setWebBindingInitializer(webBindingInitializer());
			}};
		}
		
		@Bean public WebBindingInitializer webBindingInitializer() {
			return new WebBindingInitializer() {
				public void initBinder(WebDataBinder binder, WebRequest request) {
					binder.registerCustomEditor(Level.class,new LevelPropertyEditor());
				}
			};
		}
	}
	
	
	/**
	 *     @Configuration 부분을 XML로 수정시
     *
	 *	 static class MyWebBindingInitializer implements WebBindingInitializer{
	 *		@Override
	 *		public void initBinder(WebDataBinder binder, WebRequest request) {
	 *			// TODO Auto-generated method stub
	 *			binder.registerCustomEditor(Level.class,new LevelPropertyEditor());
	 *		}
	 *	 }
	 *	
	 *		<bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter">
	 *			<property name="webBindingInitializer">
	 *				<bean class="com.raon.web.atmvc.BindingTest$MyWebBindingInitializer" />
	 *			</property>
	 *		</bean>
	 */
	
	
	
}//end BindingTest
