package com.hau.ketnguyen.service;

import com.hau.ketnguyen.entity.SecureTokenEntity;

public interface ISecureTokenService {
	SecureTokenEntity createSecureToken();
	void saveSecureToken(SecureTokenEntity secureTokenEntity);
	SecureTokenEntity findByToken(String token);
	void removeToken(SecureTokenEntity token);
	void removeTokenByToken(String token);
}
