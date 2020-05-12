package com.raon.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.raon.domain.Login;

@Controller
@RequestMapping("/login")
public class LoginControlelr {
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String showform(ModelMap model) {
		model.addAttribute(new Login());
		return "login";
		
	}

}
