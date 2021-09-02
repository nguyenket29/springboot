package com.hau.ketnguyen.service.impl;

import java.util.Objects;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hau.ketnguyen.context.ForgotPasswordEmailContext;
import com.hau.ketnguyen.entity.SecureTokenEntity;
import com.hau.ketnguyen.entity.UserEntity;
import com.hau.ketnguyen.exception.InvalidTokenException;
import com.hau.ketnguyen.exception.UnkownIdentifierException;
import com.hau.ketnguyen.repository.SecureTokenRepository;
import com.hau.ketnguyen.repository.UserRepository;
import com.hau.ketnguyen.service.IPasswordService;
import com.hau.ketnguyen.service.ISecureTokenService;
import com.hau.ketnguyen.service.ISendMailService;
import com.hau.ketnguyen.service.IUserService;

@Service
public class PasswordServiceImpl implements IPasswordService{
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISecureTokenService secureTokenService;
	
	@Autowired
	private SecureTokenRepository secureRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ISendMailService sendMail;
	
	@Value("${site.base.url.https}")
    private String baseURL;

	@Override
	public void forgottenPassword(String userName) throws UnkownIdentifierException {
		UserEntity user = userService.getUserById(userName);
		sendResetPasswordEmail(user);
	}
	
	protected void sendResetPasswordEmail(UserEntity user) {
        SecureTokenEntity secureToken= secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            sendMail.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void updatePassword(String password, String token) throws InvalidTokenException, UnkownIdentifierException {
		SecureTokenEntity secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        UserEntity user = userRepository.getOne(secureToken.getUser().getId());
        if(Objects.isNull(user)){
            throw new UnkownIdentifierException("unable to find user for the token");
        }
        secureTokenService.removeToken(secureToken);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
	}

	@Override
	public boolean loginDisabled(String email) {
		UserEntity user = userRepository.findByEmail(email);
		return user != null ? user.isLoginDisabled() : false;
	}
	
}
