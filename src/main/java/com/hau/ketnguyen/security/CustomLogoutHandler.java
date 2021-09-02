package com.hau.ketnguyen.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class CustomLogoutHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		try {
			authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication != null) {
				new SecurityContextLogoutHandler().logout(request, response, authentication);
			}
			response.sendRedirect("/logout");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
