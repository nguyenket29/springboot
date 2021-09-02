package com.hau.ketnguyen.service;

import com.hau.ketnguyen.exception.InvalidTokenException;
import com.hau.ketnguyen.exception.UnkownIdentifierException;

public interface IPasswordService {
	void forgottenPassword(final String userName) throws UnkownIdentifierException;
    void updatePassword(final String password, final String token) throws InvalidTokenException, UnkownIdentifierException;
    boolean loginDisabled(final String username);
}
