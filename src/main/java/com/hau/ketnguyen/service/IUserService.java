package com.hau.ketnguyen.service;

import com.hau.ketnguyen.entity.UserData;
import com.hau.ketnguyen.entity.UserEntity;
import com.hau.ketnguyen.exception.InvalidTokenException;
import com.hau.ketnguyen.exception.UnkownIdentifierException;
import com.hau.ketnguyen.exception.UserAlreadyExistException;

public interface IUserService {
	void register(UserData user) throws UserAlreadyExistException;
	
	boolean checkUserExist(String mail);
	
	void sendRegistrationConfirmationEmail(UserEntity userEntity);
	
	boolean verifyUser(String token) throws InvalidTokenException;
	
	UserEntity getUserById(final String id) throws UnkownIdentifierException;
}
