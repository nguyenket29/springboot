package com.hau.ketnguyen.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping({"/login"})
	public String login() {
		return "account/login";
	}
	
	@GetMapping({"/logout"})
	public String logout(HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		SecurityContextHolder.clearContext();
		httpSession = request.getSession(false);
		if(httpSession != null) {
			httpSession.invalidate();
		}
		
		for(Cookie cookie : request.getCookies()) {
			cookie.setMaxAge(0);
		}
		return "account/login";
	}
}
