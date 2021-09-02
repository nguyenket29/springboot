package com.hau.ketnguyen.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hau.ketnguyen.entity.ResetPasswordData;
import com.hau.ketnguyen.exception.InvalidTokenException;
import com.hau.ketnguyen.exception.UnkownIdentifierException;
import com.hau.ketnguyen.service.IPasswordService;

@Controller
@RequestMapping("/password")
public class PasswordController {
	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String MSG = "resetPasswordMsg";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IPasswordService passService;

	@GetMapping("/request")
	public String forgot(Model model) {
		model.addAttribute("forgotPassword", new ResetPasswordData());
		return "account/forgotPassword";
	}

	@PostMapping("/request")
	public String resetPassword(Model model,
			@ModelAttribute(value = "forgotPassword") ResetPasswordData res) {
		try {
			passService.forgottenPassword(res.getEmail());
		} catch (UnkownIdentifierException e) {
			e.getCause();
		}
		model.addAttribute(MSG, messageSource.getMessage("user.forgotpwd.msg", null, LocaleContextHolder.getLocale()));
		return "account/login";
	}

	@GetMapping("/change")
	public String changePass(@RequestParam(required = false) String token, Model model) {
		if (StringUtils.isEmpty(token)) {
			model.addAttribute("tokenError", messageSource.getMessage("user.registration.verification.missing.token",
					null, LocaleContextHolder.getLocale()));
			return REDIRECT_LOGIN;
		}

		ResetPasswordData data = new ResetPasswordData();
		data.setToken(token);
		setResetPasswordForm(model, data);

		return "account/changePassword";
	}

	@PostMapping("/change")
	public String changePassword(ResetPasswordData data, Model model) {
		try {
			passService.updatePassword(data.getPassword(), data.getToken());
		} catch (InvalidTokenException | UnkownIdentifierException e) {
			// log error statement
			model.addAttribute("tokenError", messageSource.getMessage("user.registration.verification.invalid.token",
					null, LocaleContextHolder.getLocale()));

			return "account/changePassword";
		}
		model.addAttribute("passwordUpdateMsg",
				messageSource.getMessage("user.password.updated.msg", null, LocaleContextHolder.getLocale()));
		setResetPasswordForm(model, new ResetPasswordData());
		return "account/changePassword";
	}

	private void setResetPasswordForm(Model model, ResetPasswordData data) {
		model.addAttribute("forgotPassword", data);
	}
}
