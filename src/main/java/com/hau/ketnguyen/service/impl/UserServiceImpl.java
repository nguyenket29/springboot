package com.hau.ketnguyen.service.impl;

import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hau.ketnguyen.context.VerificationEmailContext;
import com.hau.ketnguyen.entity.RoleEntity;
import com.hau.ketnguyen.entity.SecureTokenEntity;
import com.hau.ketnguyen.entity.UserData;
import com.hau.ketnguyen.entity.UserEntity;
import com.hau.ketnguyen.exception.InvalidTokenException;
import com.hau.ketnguyen.exception.UserAlreadyExistException;
import com.hau.ketnguyen.repository.RoleRepository;
import com.hau.ketnguyen.repository.SecureTokenRepository;
import com.hau.ketnguyen.repository.UserRepository;
import com.hau.ketnguyen.service.ISecureTokenService;
import com.hau.ketnguyen.service.ISendMailService;
import com.hau.ketnguyen.service.IUserService;
import com.hau.ketnguyen.exception.UnkownIdentifierException;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ISecureTokenService secureToken;
	
	@Autowired
	private SecureTokenRepository secureTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SecureTokenRepository secureTokenRepo;
	
	@Autowired
	private ISendMailService sendMailService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Value("${site.base.url.https}")
    private String baseURL;

	@Override
	public void register(UserData user) throws UserAlreadyExistException{
		if(checkUserExist(user.getEmail())){
            throw new UserAlreadyExistException("User already exists for this email");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        encodePassword(userEntity, user);
        updateUserRole(userEntity);
        userRepository.save(userEntity);
        sendRegistrationConfirmationEmail(userEntity);
	}
	
	public void updateUserRole(UserEntity userEntity) {
		RoleEntity roleEntity = roleRepository.findByCode("USER");
		userEntity.addUserRole(roleEntity);
	}

	@Override
	public boolean checkUserExist(String mail){
		return userRepository.findByEmail(mail) != null ? true : false;
	}
	
	private void encodePassword(UserEntity entity,UserData data) {
		entity.setPassword(passwordEncoder.encode(data.getPassword()));
	}

	@Override
	public void sendRegistrationConfirmationEmail(UserEntity userEntity) {
		SecureTokenEntity tokenEntity = secureToken.createSecureToken();
		tokenEntity.setUser(userEntity);
		secureTokenRepository.save(tokenEntity);
		
		VerificationEmailContext emailContext = new VerificationEmailContext();
		emailContext.init(userEntity);
		emailContext.setToken(tokenEntity.getToken());
		emailContext.buildVerificationUrl(baseURL, tokenEntity.getToken());
		try {
			sendMailService.sendMail(emailContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean verifyUser(String token) throws InvalidTokenException {
		SecureTokenEntity secureTokenEntity = secureTokenRepo.findByToken(token);
		if(Objects.isNull(secureTokenEntity) || !StringUtils.equals(token, secureTokenEntity.getToken()) || secureTokenEntity.isExpired()) {
			throw new InvalidTokenException("Token is not valid !");
		}
		
		UserEntity user = userRepository.getOne(secureTokenEntity.getUser().getId());
		if(Objects.isNull(user)) {
			return false;
		}
		user.setAccountVerified(true);
		userRepository.save(user);
		
		secureToken.removeToken(secureTokenEntity);
		return true;
	}

	@Override
	public UserEntity getUserById(String id) throws UnkownIdentifierException {
		UserEntity user = userRepository.findByEmail(id);
		if(user == null || BooleanUtils.isFalse(user.isAccountVerified())) {
			throw new UnkownIdentifierException("unable to find account or account is not active");
		}
		return user;
	}
}
