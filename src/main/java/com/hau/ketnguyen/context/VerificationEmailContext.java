package com.hau.ketnguyen.context;

import org.springframework.web.util.UriComponentsBuilder;

import com.hau.ketnguyen.entity.UserEntity;

public class VerificationEmailContext extends AbstractEmailContext {
	private String token;

	@Override
	public <T> void init(T context) {
		UserEntity user = (UserEntity) context;
		put("firstName", user.getFirstName());
		setTemplateLocation("account/email-vertification");
		setSubject("Complete your registration");
		setFrom("ketmax121@gmail.com");
		setTo(user.getEmail());
	}

	public void setToken(String token) {
		this.token = token;
		put("token", token);
	}

	public void buildVerificationUrl(String baseURL,String token) {
		String url = UriComponentsBuilder.fromHttpUrl(baseURL).path("/register/verify").queryParam("token", token)
				.toUriString();
		put("verificationURL", url);
	}

}
