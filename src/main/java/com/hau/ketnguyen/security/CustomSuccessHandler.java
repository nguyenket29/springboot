package com.hau.ketnguyen.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private final GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ADMIN");

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (isAdminAuthority(authentication)) {
			String targetUrl = "/admin/home";
			clearAuthenticationAttributes(request);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		} else {
			String targetUrl = super.determineTargetUrl(request, response);
			if (StringUtils.isEmpty(targetUrl) || StringUtils.equals(targetUrl, "/")) {
				targetUrl = "/account/home";
			}
			clearAuthenticationAttributes(request);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);

		}
	}

	protected boolean isAdminAuthority(final Authentication authentication) {
		return CollectionUtils.isNotEmpty(authentication.getAuthorities())
				&& authentication.getAuthorities().contains(adminAuthority);
	}
}
