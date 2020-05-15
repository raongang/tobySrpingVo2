package com.raon.spring31.web;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *  UriComponentsBuilder -> URI/ URL 정보를 추상화한 UriComponent를 쉽게 생성할수 있게
 *  도와주는 빌더클래스
 *  
 *  UriComponentsBuilder를 이용하면 문자열 하나에 여러 요소가 조합된 URI를 안전하고 편리하게 다룰 수 있다.
 * 
 * @author raongang
 *
 */
public class UriComponentBuilderTest {

	@Test
	public void uriCB() {
		/* 만약 컨트롤러에서 현재 요청의 URI에 대한 정보를 얻거나 URI를 조작해서 새로운 URI를 생성하고 싶다면
		 *  ServletUriComponentBuilder.fromRequest()를 이용
		 *  
		 *  Spring3.1 적용
		 *    - 파라미터에 넣으면 현재 요청의 URI를 기준으로 생성된 UriComponentBuilder를 받을수 있음.
		 *  @RequestMapping(...)
		 *  public String uri(UriComponentBuilder uriComponentBuilder){
		 *  
		 *   }
		 */
		
		UriComponents uriCompoenents = UriComponentsBuilder.fromUriString("http://example.com/hotels/{hotel}/booking/{booking}").build();
//		String uri = “http://www.myshop.com/user/” + userId “/order/” + orderId;
		
		int userId=10;
		int orderId=20;
		
		UriComponents uc = UriComponentsBuilder.fromUriString("http://www.myshop.com/users/{user}/orders/{order}").build();
		System.out.println(uc.expand(10,20).encode().toUriString());
		
		UriComponents uc2 = UriComponentsBuilder.newInstance()
				.scheme("http")
				.host("www.myshope.com")
				.path("/users/{user}/orders/{order}").build();
		
		System.out.println(uc2.expand(30,40).encode().toUriString());
		
	}
}
