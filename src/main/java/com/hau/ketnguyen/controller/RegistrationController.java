package com.hau.ketnguyen.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hau.ketnguyen.entity.UserData;
import com.hau.ketnguyen.exception.InvalidTokenException;
import com.hau.ketnguyen.exception.UserAlreadyExistException;
import com.hau.ketnguyen.service.IUserService;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	@Autowired
	private IUserService userService;

	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public String register(Model model) {
		model.addAttribute("userData", new UserData());
		return "account/register";
	}

	@PostMapping
	public String userRegistration(@Valid UserData userData, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("registrationForm", userData);
			return "account/register";
		}

		try {
			userService.register(userData);
		} catch (UserAlreadyExistException e) {
			bindingResult.rejectValue("email", "userData.email", "An account already exists for this email.");
			model.addAttribute("registrationForm", userData);
			return "account/register";
		}
		model.addAttribute("registrationMsg", messageSource.getMessage("user.registration.verification.email.msg", null,
				LocaleContextHolder.getLocale()));
		return "account/register";
	}

	@GetMapping("/verify")
	public String verifyCustomer(@RequestParam(required = false) String token, final Model model) {
		if (StringUtils.isEmpty(token)) {
			model.addAttribute("tokenError", messageSource
					.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale()));
			return "account/login";
		}
		try {
			userService.verifyUser(token);
		} catch (InvalidTokenException e) {
			model.addAttribute("tokenError", messageSource
					.getMessage("user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale()));
			return "account/login";
		}

		model.addAttribute("verifiedAccountMsg", messageSource
				.getMessage("user.registration.verification.success", null, LocaleContextHolder.getLocale()));
		return "account/login";
	}
}
