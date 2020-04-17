package com.raon.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	/* 커스컴 프로퍼티 에디터 */
	@Test
	public void levelPropertyEditor() {
		LevelPropertyEditor levelEditor = new LevelPropertyEditor();
		
		levelEditor.setValue(Level.BASIC);
		assertThat(levelEditor.getAsText(),is("1"));
		
		levelEditor.setAsText("3");
		assertThat((Level)levelEditor.getValue(), is(Level.GOLD));
	}
	
	static class LevelPropertyEditor extends PropertyEditorSupport {
		public String getAsText() {
			return String.valueOf(((Level)this.getValue()).intValue());
		}

		public void setAsText(String text) throws IllegalArgumentException {
			this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
		}
	}
	
	/* @InitBinder 테스트 */
	@Test
	public void levelTypeParameter() throws ServletException, IOException{
		
		setClasses(SearchController.class);
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
			dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		}
		
		@RequestMapping("/user/search")
		public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level",level);
		}
	}
	
	
	
}
